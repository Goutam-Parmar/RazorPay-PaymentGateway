package com.goutam.razorpay.vault.service.Impl;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.common.enums.CardBand;
import com.goutam.razorpay.common.exception.ResourceNotFoundException;
import com.goutam.razorpay.common.util.RandomizerUtil;
import com.goutam.razorpay.payment.processor.PaymentProcessorRouter;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;
import com.goutam.razorpay.vault.config.VaultEncryptionConfig;
import com.goutam.razorpay.vault.dto.request.TokenizeRequest;
import com.goutam.razorpay.vault.dto.response.TokenizeResponse;
import com.goutam.razorpay.vault.entity.CardToken;
import com.goutam.razorpay.vault.entity.VaultCard;
import com.goutam.razorpay.vault.repository.CardTokenRepository;
import com.goutam.razorpay.vault.repository.VaultCardRepository;
import com.goutam.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {


    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor decEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {


        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);
        CardBand cardBand =  detectBand(request.pan());

   byte[] dek = KeyGenerators.secureRandom(32).generateKey();
byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
        .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));

byte[] encryptedDek = decEncryptor.encrypt(dek);

        VaultCard vaultCard = vaultCardRepository.save(VaultCard.builder()
                .brand(cardBand)
                .expiryYear(request.expiryYear().toString())
                .expiryMonth(request.expiryMonth().toString())
                .bin(bin)
                .lastFour(lastFour)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .cardHolderName(request.cardHolderName())
                .build());
        String token = "tok_" + RandomizerUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                .vaultCard(vaultCard)
                .token(token)
                .customer(request.customerId())
                .merchant(merchantId)
                .build());

        return new TokenizeResponse(token, lastFour, cardBand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    @Transactional
    public PaymentProcessorResponseDto charge(UUID paymentId, String token,
                                              Money amount, Map<String, Object> methodDetails) {
        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;

        try {
            byte[] dek = decEncryptor.decrypt(vaultCard.getEncryptedDek());
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panBytes, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

            PaymentProcessorRequestDto paymentProcessorRequest = PaymentProcessorRequestDto
                    .card(paymentId, pan, expiry, amount, methodDetails);

            PaymentProcessorResponseDto response = paymentProcessorRouter.charge(paymentProcessorRequest);

            log.info("Vault charge registered, token={}****", token.substring(0, 4));

            return response;
        } catch (Exception e) {
            log.warn("Vault charge failed, token={}****", token.substring(0, 4));
            return new PaymentProcessorResponseDto.Failure("VAULT_CHARGE_FAILED", e.getMessage());
        } finally {
            if (panBytes != null) Arrays.fill(panBytes, (byte) 0);
        }
    }
    public CardBand detectBand(String pan) {
        String bin = pan.substring(0, 6);
        if (bin.startsWith("4")) {
            return CardBand.VISA;
        } else if (bin.startsWith("5") || bin.startsWith("2")) {
            return CardBand.MASTERCARD;
        } else if (bin.startsWith("37") || bin.startsWith("34")) {
            return CardBand.AMEX;

        } else {
            return CardBand.RUPAY;
        }
    }
}

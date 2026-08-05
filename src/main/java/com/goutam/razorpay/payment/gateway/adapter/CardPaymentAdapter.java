package com.goutam.razorpay.payment.gateway.adapter;

import com.goutam.razorpay.payment.gateway.PaymentAdapter;
import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;
import com.goutam.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@RequiredArgsConstructor
@Component
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    @Override
    public PaymentResultDto initiate(PaymentRequest request) {
        String token = (String) request.methodDetails().get("token");

        PaymentProcessorResponseDto response = vaultService.charge(
                request.paymentId(), token, request.amount(), request.methodDetails()
        );

        return switch (response) {
            case PaymentProcessorResponseDto.Success success -> new PaymentResultDto.Success(success.bankReference());
            case PaymentProcessorResponseDto.Failure failure -> new PaymentResultDto.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponseDto.Pending pending -> new PaymentResultDto.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResultDto capture(UUID paymentId) {
        return new PaymentResultDto.Success("CARD_REF");
    }
}

package com.goutam.razorpay.vault.service.Impl;

import com.goutam.razorpay.vault.dto.response.TokenizeResponse;
import com.goutam.razorpay.vault.repository.CardTokenRepository;
import com.goutam.razorpay.vault.repository.VaultCardRepository;
import com.goutam.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {


    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;

    @Override
    public TokenizeResponse tokenize(TokenizeResponse request, UUID merchantId) {

        return request;
    }
}

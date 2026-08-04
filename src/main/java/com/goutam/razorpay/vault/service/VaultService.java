package com.goutam.razorpay.vault.service;

import com.goutam.razorpay.vault.dto.response.TokenizeResponse;

import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeResponse request, UUID merchantId);
}

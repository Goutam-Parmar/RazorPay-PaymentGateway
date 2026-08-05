package com.goutam.razorpay.vault.service;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;
import com.goutam.razorpay.vault.dto.request.TokenizeRequest;
import com.goutam.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);
    PaymentProcessorResponseDto charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}

package com.goutam.razorpay.merchant.service;


import com.goutam.razorpay.merchant.dto.request.CreateApiKeyRequestDto;
import com.goutam.razorpay.merchant.dto.response.ApiKeyCreateResponseDto;
import com.goutam.razorpay.merchant.dto.response.ApiKeyResponseDto;
import org.jspecify.annotations.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface ApiKeyService {
    ApiKeyCreateResponseDto create(UUID merchantId, CreateApiKeyRequestDto request);

    List<ApiKeyResponseDto> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    @Nullable
    ApiKeyCreateResponseDto rotate(UUID merchantId, UUID keyId);
}

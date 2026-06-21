package com.goutam.razorpay.merchant.dto.response;

import com.goutam.razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponseDto(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}

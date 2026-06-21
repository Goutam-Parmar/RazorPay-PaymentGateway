package com.goutam.razorpay.merchant.dto.response;

import com.goutam.razorpay.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponseDto(
        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt
) {
}

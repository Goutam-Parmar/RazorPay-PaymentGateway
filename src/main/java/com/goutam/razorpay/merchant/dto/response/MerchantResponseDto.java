package com.goutam.razorpay.merchant.dto.response;

import com.goutam.razorpay.common.enums.BusinessType;
import com.goutam.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponseDto(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {
}

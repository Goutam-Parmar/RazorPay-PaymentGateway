package com.goutam.razorpay.merchant.dto.request;

import com.goutam.razorpay.common.enums.Environment;

public record CreateApiKeyRequestDto(
        Environment environment
) {

}

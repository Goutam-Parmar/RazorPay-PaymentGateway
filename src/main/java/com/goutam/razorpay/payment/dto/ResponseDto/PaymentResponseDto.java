package com.goutam.razorpay.payment.dto.ResponseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.common.enums.PaymentMethod;
import com.goutam.razorpay.common.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponseDto(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt
) {
}

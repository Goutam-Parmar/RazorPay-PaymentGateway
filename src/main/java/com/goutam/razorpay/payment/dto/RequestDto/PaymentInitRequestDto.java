package com.goutam.razorpay.payment.dto.RequestDto;

import com.goutam.razorpay.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record PaymentInitRequestDto(

        @NotNull(message = "Order ID cannot be null")
        UUID orderId,

        @NotNull(message = "Payment method cannot be null")
        PaymentMethod method,

        Map<String ,Object> methodDetails

) {
}

package com.goutam.razorpay.payment.gateway.dto;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(

        UUID merchantId,
        UUID orderId,
        UUID paymentId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails

) {
}

package com.goutam.razorpay.payment.processor.dto;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.Objects;

public record PaymentProcessorRequestDto (

        PaymentMethod method,
        Money amount,
        String pan,
        String expiry,
        Map<String, Objects> methodDetails
){
}

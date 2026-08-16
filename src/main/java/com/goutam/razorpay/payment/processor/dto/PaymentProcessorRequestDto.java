package com.goutam.razorpay.payment.processor.dto;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.common.enums.PaymentMethod;
import com.goutam.razorpay.payment.processor.PaymentProcessorRouter;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record PaymentProcessorRequestDto (
        UUID paymentId,
        UUID processingId,
        PaymentMethod method,
        Money amount,
        String pan,
        String expiry,
        Map<String, Object> methodDetails
){

    public static PaymentProcessorRequestDto card (UUID paymentId, String pan , String expiry, Money amount, Map<String, Object> details){
        return new PaymentProcessorRequestDto(UUID.randomUUID(), paymentId
                ,PaymentMethod.CARD, amount,
                pan, expiry, details);

    }

    public static PaymentProcessorRequestDto nonCard (UUID paymentId , PaymentMethod method ,  Money amount, Map<String, Object> details){
        return new PaymentProcessorRequestDto(UUID.randomUUID(), paymentId
                ,method, amount,null,null, details);

    }
}

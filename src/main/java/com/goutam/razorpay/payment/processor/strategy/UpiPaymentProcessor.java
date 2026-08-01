package com.goutam.razorpay.payment.processor.strategy;

import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;

public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponseDto charge(PaymentProcessorRequestDto request) {
        // Implement UPI payment processing logic here
        return new PaymentProcessorResponseDto.Success("upiProcessorReference123", "upiBankReference456");
    }
}

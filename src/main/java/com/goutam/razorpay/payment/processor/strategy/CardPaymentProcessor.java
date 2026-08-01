package com.goutam.razorpay.payment.processor.strategy;

import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;

public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponseDto charge(PaymentProcessorRequestDto request) {

        return new PaymentProcessorResponseDto.Success("processorReference123", "bankReference456");
    }
}

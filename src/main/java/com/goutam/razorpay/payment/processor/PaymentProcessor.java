package com.goutam.razorpay.payment.processor;

import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;

public interface PaymentProcessor {

    PaymentProcessorResponseDto charge(PaymentProcessorRequestDto request);
}

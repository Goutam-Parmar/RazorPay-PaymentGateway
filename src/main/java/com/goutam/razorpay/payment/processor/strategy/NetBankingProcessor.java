package com.goutam.razorpay.payment.processor.strategy;

import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;

public class NetBankingProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponseDto charge(PaymentProcessorRequestDto request) {
        // Implement the logic for processing net banking payments here
        // For demonstration purposes, we'll return a success response
        return new PaymentProcessorResponseDto.Success("netBankingProcessorReference123", "netBankingBankReference456");
    }
}

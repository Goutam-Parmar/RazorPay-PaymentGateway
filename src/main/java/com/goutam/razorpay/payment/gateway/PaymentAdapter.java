package com.goutam.razorpay.payment.gateway;

import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;

import java.util.UUID;

public interface PaymentAdapter {

    PaymentResultDto initiate(PaymentRequest request);

    PaymentResultDto capture(UUID paymentId);
}

package com.goutam.razorpay.payment.gateway;

import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;

public interface PaymentAdapter {

    PaymentResultDto initiate(PaymentRequest request);
}

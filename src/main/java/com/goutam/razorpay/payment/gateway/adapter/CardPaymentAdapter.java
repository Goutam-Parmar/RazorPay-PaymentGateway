package com.goutam.razorpay.payment.gateway.adapter;

import com.goutam.razorpay.payment.gateway.PaymentAdapter;
import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@RequiredArgsConstructor
@Component
public class CardPaymentAdapter implements PaymentAdapter {

    @Override
    public PaymentResultDto initiate(PaymentRequest request){
return null;
    }

    @Override
    public PaymentResultDto capture(UUID paymentId) {
        return null;
    }

}

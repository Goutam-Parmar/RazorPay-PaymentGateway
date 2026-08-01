package com.goutam.razorpay.payment.config;

import com.goutam.razorpay.common.enums.PaymentMethod;
import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.goutam.razorpay.payment.processor.strategy.NetBankingProcessor;
import com.goutam.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap() {
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.UPI, new UpiPaymentProcessor(),
                PaymentMethod.NETBANKING, new NetBankingProcessor()
        );
    }
}

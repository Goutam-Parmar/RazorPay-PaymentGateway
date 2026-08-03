package com.goutam.razorpay.payment.gateway;

import com.goutam.razorpay.common.enums.PaymentMethod;
import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {


    private final Map<PaymentMethod, PaymentAdapter> paymentAdapterMap;
   public PaymentResultDto initiate(PaymentRequest request){

       PaymentAdapter adapter = paymentAdapterMap.get(request.method());
       if(adapter == null){
           throw new IllegalArgumentException("No payment adapter register  for payment method: " + request.method());
       }
      return adapter.initiate(request);

    }

    public PaymentResultDto capture(PaymentMethod method, UUID paymentId) {
        PaymentAdapter adapter = paymentAdapterMap.get(method);
        if(adapter == null){
            throw new IllegalArgumentException("No payment adapter register  for payment method: " + method);
        }
        return adapter.capture(paymentId);
    }
}

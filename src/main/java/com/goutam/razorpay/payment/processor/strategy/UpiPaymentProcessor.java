package com.goutam.razorpay.payment.processor.strategy;

import com.goutam.razorpay.common.util.RandomizerUtil;
import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;

public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponseDto charge(PaymentProcessorRequestDto request) {


        final String VPA_CODE_FAIL = "fail@okaix";

        String bankCode = request.methodDetails() != null?
                (String) request.methodDetails().get("vpa") : null;


        // simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponseDto.Failure("BANK_CODE_FAIL", "Bank code is invalid");
        }

        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        //String redirectUrl = "https://bank.com/redirect?processorRef=" + processorRef;

        return new PaymentProcessorResponseDto.Pending(processorRef);


    }
}

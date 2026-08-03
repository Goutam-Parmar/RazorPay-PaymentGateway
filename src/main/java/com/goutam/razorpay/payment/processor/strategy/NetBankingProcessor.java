package com.goutam.razorpay.payment.processor.strategy;

import com.goutam.razorpay.common.util.RandomizerUtil;
import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;

public class NetBankingProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponseDto charge(PaymentProcessorRequestDto request) {

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null?
    (String) request.methodDetails().get("BANK") : null;


        // simulation
        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponseDto.Failure("BANK_CODE_FAIL", "Bank code is invalid");
        }

        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        String redirectUrl = "https://bank.com/redirect?processorRef=" + processorRef;

        return new PaymentProcessorResponseDto.Success(processorRef,redirectUrl);



    }
}

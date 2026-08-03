package com.goutam.razorpay.payment.gateway.adapter;

import com.goutam.razorpay.common.enums.PaymentMethod;
import com.goutam.razorpay.payment.gateway.PaymentAdapter;
import com.goutam.razorpay.payment.gateway.PaymentGatewayRouter;
import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;
import com.goutam.razorpay.payment.processor.PaymentProcessor;
import com.goutam.razorpay.payment.processor.PaymentProcessorRouter;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorRequestDto;
import com.goutam.razorpay.payment.processor.dto.PaymentProcessorResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpiPaymentAdapter implements PaymentAdapter {


    private final PaymentProcessorRouter paymentProcessorRouter;



    @Override
    public PaymentResultDto initiate(PaymentRequest request) {

        log.info("Initiating UPI payment Adapter , paymentId: {}", request.paymentId());
        try {


            PaymentProcessorRequestDto paymentProcessorRequest = PaymentProcessorRequestDto.nonCard(
                    request.paymentId(),
                    PaymentMethod.UPI,
                    request.amount(),
                    request.methodDetails()
            );

            PaymentProcessorResponseDto paymentProcessorResponse =
                    paymentProcessorRouter.charge(paymentProcessorRequest);


            return switch (paymentProcessorResponse) {
                case PaymentProcessorResponseDto.Failure failure ->
                        new PaymentResultDto.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponseDto.Pending pending ->
                        new PaymentResultDto.Pending(pending.processorReference());
                case PaymentProcessorResponseDto.Success success ->
                        new PaymentResultDto.Success(success.bankReference());
            };
        }catch (Exception e){
            log.warn("UPI failed , paymentId:{}", request.paymentId());
            return new PaymentResultDto.Failure("UPI_ADAPTER_Failed", e.getMessage());
        }
    }

    @Override
    public PaymentResultDto capture(UUID paymentId) {
        return new PaymentResultDto.Success("UPI Reference");
    }
}

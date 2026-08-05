package com.goutam.razorpay.payment.service.impl;

import com.goutam.razorpay.common.enums.EventAggregateType;
import com.goutam.razorpay.common.enums.OrderStatus;
import com.goutam.razorpay.common.enums.PaymentEvent;
import com.goutam.razorpay.common.enums.PaymentStatus;
import com.goutam.razorpay.common.exception.BusinessRuleViolationException;
import com.goutam.razorpay.common.exception.ResourceNotFoundException;
import com.goutam.razorpay.payment.dto.RequestDto.PaymentInitRequestDto;
import com.goutam.razorpay.payment.dto.ResponseDto.PaymentResponseDto;
import com.goutam.razorpay.payment.entity.OrderRecord;
import com.goutam.razorpay.payment.entity.Payment;
import com.goutam.razorpay.payment.gateway.PaymentGatewayRouter;
import com.goutam.razorpay.payment.gateway.dto.PaymentRequest;
import com.goutam.razorpay.payment.gateway.dto.PaymentResultDto;
import com.goutam.razorpay.payment.mapper.PaymentMapper;
import com.goutam.razorpay.payment.outbox.OutboxEventPublisher;
import com.goutam.razorpay.payment.repository.OrderRepository;
import com.goutam.razorpay.payment.repository.PaymentRepository;
import com.goutam.razorpay.payment.service.PaymentService;
import com.goutam.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor

public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;
    private final OutboxEventPublisher eventPublisher;

    @Transactional
    @Override
    public PaymentResponseDto initiate(UUID merchantId, PaymentInitRequestDto request) {

        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if(order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED) {
            throw new BusinessRuleViolationException("Order_Not_Payable","Order Can not Payable in status :"+order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        Payment payment= Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(
                merchantId,
                request.orderId(),
                payment.getId(),
                order.getAmount(),
                request.method(),
                request.methodDetails()
        );
      PaymentResultDto result =  paymentGatewayRouter.initiate(paymentRequest);

  switch (result){
      case PaymentResultDto.Pending pending -> payment.setProcessorReference(pending.registrationRef());
      case PaymentResultDto.Failure  failure -> {
          //payment.setStatus(PaymentStatus.FAILED);
          paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
          payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
      }
      case PaymentResultDto.Success success -> {

      }
  }
  payment = paymentRepository.save(payment);
  orderRepository.save(order);


  // TODO :send an outbox(kafka)

        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponseDto capture(UUID merchantId, UUID paymentId) {

        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));


 paymentTransitionService .apply(payment, PaymentEvent.CAPTURE_REQUEST);
        PaymentResultDto paymentResult = paymentGatewayRouter.capture(payment.getMethod(),paymentId);

        if(paymentResult instanceof PaymentResultDto.Success success) {

            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment Captured successfully for paymentId: {}, merchantId: {}", paymentId, merchantId);
        } else if (paymentResult instanceof PaymentResultDto.Failure failure) {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
    log.warn("Payment Capture failed for paymentId: {}",paymentId);
        }

        payment = paymentRepository.save(payment);


        // TODO :send an outbox(kafka event)
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }





    @Override
    @Transactional
    public void resolveAuthorization(UUID paymentId, boolean approve,
                                     String bankRef, String errorCode, String errorDescription) {

//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        Payment payment = paymentRepository.findByIdForUpdate(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        if (payment.getStatus() != PaymentStatus.AUTHORIZING) {
            log.warn("Payment is not in Authorizing state, paymentID: {}, status: {}", paymentId, payment.getStatus());
            return;
        }

        OrderRecord orderRecord = payment.getOrder();

        if (approve) {
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_SUCCESS);
            payment.setBankReference(bankRef);
            payment.setAuthorizedAt(LocalDateTime.now());

            // Auto-capture
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);
            PaymentResultDto captureResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

            if(captureResult instanceof PaymentResultDto.Success success) {
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
                payment.setCapturedAt(LocalDateTime.now());
                orderRecord.setOrderStatus(OrderStatus.PAID);
            } else if (captureResult instanceof  PaymentResultDto.Failure failure){
                paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
        } else {
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
            payment.setErrorCode(errorCode);
            payment.setErrorDescription(errorDescription);
        }

        paymentRepository.save(payment);
        orderRepository.save(orderRecord);

        eventPublisher.publish(EventAggregateType.PAYMENT, payment.getId(), "PAYMENT_STATUS_CHANGED",
                Map.of("orderId", payment.getOrder().getId().toString(),
                        "paymentId", payment.getId().toString(),
                        "merchantId", payment.getMerchantId().toString(),
                        "paymentStatus", payment.getStatus().name(),
                        "amountUnits", payment.getAmount().getAmountUnits(),
                        "amountCurrency", payment.getAmount().getCurrency(),
                        "paymentMethod", payment.getMethod()
                )
        );
    }
}

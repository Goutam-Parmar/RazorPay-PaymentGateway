package com.goutam.razorpay.payment.service.impl;

import com.goutam.razorpay.common.enums.OrderStatus;
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
import com.goutam.razorpay.payment.repository.OrderRepository;
import com.goutam.razorpay.payment.repository.PaymentRepository;
import com.goutam.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor

public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

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
          payment.setStatus(PaymentStatus.FAILED);
          payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
      }
  }
  payment = paymentRepository.save(payment);
  orderRepository.save(order);
        return paymentMapper.toResponse(payment);
    }
}

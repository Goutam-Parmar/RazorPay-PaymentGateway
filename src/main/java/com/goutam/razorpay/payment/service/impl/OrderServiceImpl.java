package com.goutam.razorpay.payment.service.impl;

import com.goutam.razorpay.common.enums.OrderStatus;
import com.goutam.razorpay.common.exception.DuplicateResourceException;
import com.goutam.razorpay.payment.dto.RequestDto.OrderRequestDto;
import com.goutam.razorpay.payment.dto.ResponseDto.OrderResponseDto;
import com.goutam.razorpay.payment.entity.OrderRecord;
import com.goutam.razorpay.payment.repository.OrderRepository;
import com.goutam.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private  final OrderRepository orderRepository;
    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Override
   public OrderResponseDto create(UUID merchantId, OrderRequestDto request) {
        if(request.receipt()!=null || orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE","Order with this receipt already exists for the merchant "+request.receipt());
        }

        OrderRecord order = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())

                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);
        // TODO:        publish kafka event about order creation

        return new OrderResponseDto(order.getId(),
                order.getMerchantId(),
                order.getReceipt(), order.getAmount(),
                order.getOrderStatus(), order.getAttempts(),
                order.getNotes(), order.getExpiresAt(),
                null);
    }
}

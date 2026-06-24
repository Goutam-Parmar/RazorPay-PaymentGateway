package com.goutam.razorpay.payment.service;

import com.goutam.razorpay.payment.dto.RequestDto.OrderRequestDto;
import com.goutam.razorpay.payment.dto.ResponseDto.OrderResponseDto;

import java.util.UUID;

public interface OrderService {
    OrderResponseDto create(UUID merchantId, OrderRequestDto request);
}

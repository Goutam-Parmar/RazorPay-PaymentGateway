package com.goutam.razorpay.payment.controller;

import com.goutam.razorpay.merchant.entity.Merchant;
import com.goutam.razorpay.payment.dto.RequestDto.OrderRequestDto;
import com.goutam.razorpay.payment.dto.ResponseDto.OrderResponseDto;
import com.goutam.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService OrderService;

    UUID MerchantId = UUID.fromString("dfdcea94-eacd-4d01-b47c-f0af1be9a044"); //TODO:  Replace with actual merchant ID
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
return ResponseEntity.status(HttpStatus.CREATED)
        .body(OrderService.create(MerchantId, request));
    }
}

package com.goutam.razorpay.payment.controller;

import com.goutam.razorpay.payment.dto.RequestDto.PaymentInitRequestDto;
import com.goutam.razorpay.payment.dto.ResponseDto.PaymentResponseDto;
import com.goutam.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/v1/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    UUID merchantId = UUID.fromString("f3e1c2d4-5b6a-7c8d-9e0f-1a2b3c4d5e6f");

    @PostMapping
    public ResponseEntity<PaymentResponseDto> initiate(@Valid @RequestBody PaymentInitRequestDto request) {
      //  PaymentResponseDto response = paymentService.initiate(request.getMerchantId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantId, request));
    }
}

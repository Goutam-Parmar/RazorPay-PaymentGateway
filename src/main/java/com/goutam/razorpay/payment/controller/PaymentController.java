package com.goutam.razorpay.payment.controller;

import com.goutam.razorpay.payment.dto.RequestDto.PaymentInitRequestDto;
import com.goutam.razorpay.payment.dto.ResponseDto.PaymentResponseDto;
import com.goutam.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

   // UUID merchantId = UUID.fromString("52b42879-68e8-4c5f-a75b-eb9ceeb683e2");
    UUID merchantId = UUID.fromString("dfdcea94-eacd-4d01-b47c-f0af1be9a044");

    @PostMapping
    public ResponseEntity<PaymentResponseDto> initiate(@Valid @RequestBody PaymentInitRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantId, request));
    }
    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponseDto> capture(@PathVariable UUID paymentId) {

        return ResponseEntity.ok(paymentService.capture(merchantId, paymentId));
    }
}

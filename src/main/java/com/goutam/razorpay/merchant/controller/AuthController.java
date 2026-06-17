package com.goutam.razorpay.merchant.controller;

import com.goutam.razorpay.merchant.dto.request.MerchantSignupRequestDto;
import com.goutam.razorpay.merchant.dto.response.MerchantResponseDto;
import com.goutam.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<MerchantResponseDto> signup(@RequestBody MerchantSignupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.signup(request)
        );
    }
}

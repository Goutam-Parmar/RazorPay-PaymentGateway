package com.goutam.razorpay.merchant.service;


import com.goutam.razorpay.merchant.dto.request.LoginRequest;
import com.goutam.razorpay.merchant.dto.request.MerchantSignupRequestDto;
import com.goutam.razorpay.merchant.dto.response.LoginResponse;
import com.goutam.razorpay.merchant.dto.response.MerchantResponseDto;

public interface AuthService {
    MerchantResponseDto signup(MerchantSignupRequestDto request);

    LoginResponse login(LoginRequest request);
}

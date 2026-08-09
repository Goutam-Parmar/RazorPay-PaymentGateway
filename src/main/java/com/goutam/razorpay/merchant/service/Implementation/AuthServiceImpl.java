package com.goutam.razorpay.merchant.service.Implementation;

import com.goutam.razorpay.common.enums.MerchantStatus;
import com.goutam.razorpay.common.enums.UserRole;
import com.goutam.razorpay.common.exception.DuplicateResourceException;
import com.goutam.razorpay.common.exception.ResourceNotFoundException;
import com.goutam.razorpay.merchant.dto.request.LoginRequest;
import com.goutam.razorpay.merchant.dto.request.MerchantSignupRequestDto;
import com.goutam.razorpay.merchant.dto.response.LoginResponse;
import com.goutam.razorpay.merchant.dto.response.MerchantResponseDto;
import com.goutam.razorpay.merchant.entity.AppUser;
import com.goutam.razorpay.merchant.entity.Merchant;
import com.goutam.razorpay.merchant.mapper.MerchantMapper;
import com.goutam.razorpay.merchant.repository.AppUserRepository;
import com.goutam.razorpay.merchant.repository.MerchantRepository;
import com.goutam.razorpay.merchant.security.JwtUtil;
import com.goutam.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Override
    public MerchantResponseDto signup(MerchantSignupRequestDto request) {
        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with email already exists: " + request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromSignupRequestDto(request);
       merchant.setStatus(MerchantStatus.PENDINGKYC);
       merchant = merchantRepository.save(merchant);

       AppUser appUser = AppUser.builder()
               .email(request.email())
               .passwordHash(passwordEncoder.encode(request.password()))
               .merchant(merchant)
               .role(UserRole.OWNER)
               .build();
       appUserRepository.save(appUser);
        return merchantMapper.toResponseDto(merchant);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        String token = jwtUtil.generateAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().toString());

        return new LoginResponse(token);
    }
}

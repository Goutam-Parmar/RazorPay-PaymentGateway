package com.goutam.razorpay.merchant.service.Implementation;

import com.goutam.razorpay.common.enums.MerchantStatus;
import com.goutam.razorpay.common.enums.UserRole;
import com.goutam.razorpay.merchant.dto.request.MerchantSignupRequestDto;
import com.goutam.razorpay.merchant.dto.response.MerchantResponseDto;
import com.goutam.razorpay.merchant.entity.AppUser;
import com.goutam.razorpay.merchant.entity.Merchant;
import com.goutam.razorpay.merchant.repository.AppUserRepository;
import com.goutam.razorpay.merchant.repository.MerchantRepository;
import com.goutam.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    @Override
    public MerchantResponseDto signup(MerchantSignupRequestDto request) {
        if (merchantRepository.existsByEmail(request.email())){
           throw new RuntimeException("Merchant with email already exists"+request.email());
       }

        Merchant merchant = Merchant.builder()
                .name(request.name())
                .email(request.email())
                .businessName(request.businessName())
                .businessType(request.businessType())
                .status(MerchantStatus.PENDINGKYC)
                .build();

       merchant = merchantRepository.save(merchant);

       AppUser appUser = AppUser.builder()
               .email(request.email())
               .passwordHash(request.password()) //Todo: hash the password will be implemented later
               .merchant(merchant)
               .role(UserRole.OWNER)
               .build();
       appUserRepository.save(appUser);
        return new MerchantResponseDto(merchant.getId(), merchant.getName(),
                merchant.getEmail(), merchant.getBusinessName(),
                merchant.getBusinessType(), merchant.getStatus());
    }
}

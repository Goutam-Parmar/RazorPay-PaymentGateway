package com.goutam.razorpay.merchant.mapper;

import com.goutam.razorpay.merchant.dto.request.MerchantSignupRequestDto;
import com.goutam.razorpay.merchant.dto.response.MerchantResponseDto;
import com.goutam.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    Merchant toEntityFromSignupRequestDto(MerchantSignupRequestDto request);

    MerchantResponseDto toResponseDto(Merchant merchant);
}

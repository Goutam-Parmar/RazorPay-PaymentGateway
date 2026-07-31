package com.goutam.razorpay.merchant.mapper;

import com.goutam.razorpay.merchant.dto.response.ApiKeyCreateResponseDto;
import com.goutam.razorpay.merchant.entity.APIKEY;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface APIKeyMapper {

    ApiKeyCreateResponseDto toCreateResponseDto(APIKEY apiKey);
}

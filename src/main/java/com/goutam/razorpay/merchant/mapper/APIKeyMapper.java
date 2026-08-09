package com.goutam.razorpay.merchant.mapper;

import com.goutam.razorpay.merchant.dto.response.ApiKeyCreateResponseDto;
import com.goutam.razorpay.merchant.dto.response.ApiKeyResponseDto;
import com.goutam.razorpay.merchant.entity.APIKEY;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface APIKeyMapper {

    ApiKeyCreateResponseDto toCreateResponseDto(APIKEY apiKey);

    List<ApiKeyResponseDto> toResponseList(List<APIKEY> apiKeyList);
}

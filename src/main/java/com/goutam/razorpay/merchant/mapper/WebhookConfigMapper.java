package com.goutam.razorpay.merchant.mapper;

import com.goutam.razorpay.merchant.dto.response.WebhookConfigResponseDto;
import com.goutam.razorpay.merchant.entity.MerchantWebhookConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebhookConfigMapper {
    @Mapping(target = "webhookSecret", source = "rawSecret")
    WebhookConfigResponseDto toResponse(MerchantWebhookConfig merchantWebhookConfig, String rawSecret);

}

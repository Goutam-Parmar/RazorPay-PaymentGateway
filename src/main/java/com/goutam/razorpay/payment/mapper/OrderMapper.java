package com.goutam.razorpay.payment.mapper;

import com.goutam.razorpay.payment.dto.ResponseDto.OrderResponseDto;
import com.goutam.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(target = "status", source = "orderStatus")
    OrderResponseDto toResponse(OrderRecord orderRecord);
}

package com.goutam.razorpay.payment.mapper;

import com.goutam.razorpay.payment.dto.ResponseDto.PaymentResponseDto;
import com.goutam.razorpay.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {


    @Mapping(target = "orderId", source = "order.id")
    PaymentResponseDto toResponse(Payment payment);

    @Mapping(target = "orderId", source = "order.id")
    List<PaymentResponseDto> toResponseList(List<Payment> paymentList);
}

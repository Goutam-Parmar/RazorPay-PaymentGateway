package com.goutam.razorpay.payment.repository;

import com.goutam.razorpay.payment.dto.ResponseDto.OrderResponseDto;
import com.goutam.razorpay.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);

    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);
}

package com.goutam.razorpay.operations.repository;

import com.goutam.razorpay.operations.entity.SettlementPayment;
import com.goutam.razorpay.operations.entity.SettlementPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementPaymentRepository extends JpaRepository<SettlementPayment, SettlementPaymentId> {
}
package com.goutam.razorpay.payment.service.impl;

import com.goutam.razorpay.common.enums.PaymentStatus;
import com.goutam.razorpay.payment.api.PaymentLookupService;
import com.goutam.razorpay.payment.entity.Payment;
import com.goutam.razorpay.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentLookupServiceImpl implements PaymentLookupService {


    private final PaymentRepository paymentRepository;
    @Override
    public List<Payment> findUnsettledCapturedPayments(UUID merchantId) {
        return paymentRepository.findByMerchantIdAndStatusForUpdate(merchantId, PaymentStatus.CAPTURED);
    }
}

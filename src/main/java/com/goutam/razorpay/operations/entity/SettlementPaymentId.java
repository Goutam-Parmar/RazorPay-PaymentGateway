package com.goutam.razorpay.operations.entity;

import com.goutam.razorpay.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId  {


    private UUID settlementId;

    private UUID paymentId;
}

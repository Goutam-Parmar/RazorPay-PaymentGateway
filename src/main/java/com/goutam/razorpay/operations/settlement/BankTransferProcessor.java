package com.goutam.razorpay.operations.settlement;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.operations.settlement.dto.BankTransferResultDto;

import java.util.UUID;

public interface BankTransferProcessor {

    BankTransferResultDto initiate(UUID settlementId, UUID merchantId, Money amount,
                                   String bankAccount, String ifsc);
}


package com.goutam.razorpay.common.dto;

public record SettlementBankDetailsDto(
        String accountNumber, String ifsc, String accountHolderName
) {
}

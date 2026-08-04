package com.goutam.razorpay.vault.dto.response;

import com.goutam.razorpay.common.enums.CardBand;

public record TokenizeResponse(
        String token ,
        String lastFour,
        CardBand brand,
        Integer expiryMonth,
        Integer expiryYear


) {
}

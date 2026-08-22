package com.goutam.razorpay.merchant.api;

import com.goutam.razorpay.common.dto.WebhookTargetDto;

import java.util.List;
import java.util.UUID;

public interface MerchantLookupService {
    List<WebhookTargetDto> getActiveConfigsForEvent(UUID merchantId, String eventType);
}

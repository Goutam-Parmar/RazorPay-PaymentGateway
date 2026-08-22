package com.goutam.razorpay.merchant.service;

import com.goutam.razorpay.merchant.dto.request.UpdateWebhookConfigRequestDto;
import com.goutam.razorpay.merchant.dto.response.WebhookConfigResponseDto;

import java.util.List;
import java.util.UUID;

public interface WebhookConfigService {
    WebhookConfigResponseDto create(UUID merchantId, UpdateWebhookConfigRequestDto request);

    List<WebhookConfigResponseDto> list(UUID merchantId);

    WebhookConfigResponseDto getById(UUID merchantId, UUID configId);

    WebhookConfigResponseDto update(UUID merchantId, UUID configId, UpdateWebhookConfigRequestDto request);

    void delete(UUID merchantId, UUID configId);
}

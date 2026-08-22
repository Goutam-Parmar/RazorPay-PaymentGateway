package com.goutam.razorpay.operations.webhook;


import com.goutam.razorpay.common.dto.WebhookTargetDto;
import com.goutam.razorpay.common.enums.WebhookEventStatus;
import com.goutam.razorpay.common.util.SignerUtil;
import com.goutam.razorpay.merchant.api.MerchantLookupService;
import com.goutam.razorpay.operations.entity.WebhookEvent;
import com.goutam.razorpay.operations.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookKafkaConsumer {


    private final MerchantLookupService merchantLookupService;
    private final ObjectMapper objectMapper;
    private final WebhookEventRepository webhookEventRepository;
    private final SignerUtil signerUtil;
    private final WebhookRetryQueue retryQueue;

    @KafkaListener(topics = {
            "${app.kafka.topics.payments:payments.events}",
            "${app.kafka.topics.orders:orders.events}",
            "${app.kafka.topics.refunds:refunds.events}",
            "${app.kafka.topics.settlements:settlements.events}"
    })

    public void onWebhookEvent(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack){

        Map<String, Object> envelope = record.value();
        Map<String, Object> data = (Map<String, Object>) envelope.get("data");
        String eventType = (String) envelope.get("eventType");


        Object merchantIdRaw = data.get("merchantId");
        if (merchantIdRaw == null) {
            log.warn("No merchantId was found, skipping event: {}", eventType);
            ack.acknowledge();
            return;
        }

        UUID merchantId = UUID.fromString(merchantIdRaw.toString());


        List<WebhookTargetDto> targets = merchantLookupService.getActiveConfigsForEvent(merchantId, eventType);
        if (targets.isEmpty()) {
            log.debug("No webhook target was found, skipping event: {}", eventType);
            ack.acknowledge();
            return;
        }

        Map<String, Object> signatureData = Map.of("event", eventType, "payload", data);
        String signatureJson = objectMapper.writeValueAsString(signatureData);

        for (WebhookTargetDto target : targets) {
            String signature = signerUtil.sign(signatureJson, target.webhookSecret());

            WebhookEvent webhookEvent = WebhookEvent.builder()
                    .merchantId(merchantId)
                    .eventType(eventType)
                    .payload(data)
                    .targetUrl(target.targetUrl())
                    .signature(signature)
                    .status(WebhookEventStatus.PENDING)
                    .nextRetryAt(LocalDateTime.now())
                    .build();

            webhookEvent = webhookEventRepository.save(webhookEvent);

            retryQueue.enqueue(webhookEvent.getId(), webhookEvent.getNextRetryAt());
            log.info("Created a webhook event with id: {}", webhookEvent.getId());
        }
        ack.acknowledge();


    }
}

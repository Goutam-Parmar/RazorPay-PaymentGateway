package com.goutam.razorpay.operations.repository;

import com.goutam.razorpay.operations.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, UUID> {
}

package com.goutam.razorpay.merchant.controller;

import com.goutam.razorpay.merchant.dto.request.CreateApiKeyRequestDto;
import com.goutam.razorpay.merchant.dto.response.ApiKeyCreateResponseDto;
import com.goutam.razorpay.merchant.dto.response.ApiKeyResponseDto;
import com.goutam.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/merchant/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponseDto> create(@PathVariable UUID merchantId,
                                                          @Valid @RequestBody CreateApiKeyRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponseDto>> listByMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantId));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        apiKeyService.revoke(merchantId, keyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponseDto> rotateKey(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        return ResponseEntity.ok(apiKeyService.rotate(merchantId, keyId));
    }

}

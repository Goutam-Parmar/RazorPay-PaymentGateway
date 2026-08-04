package com.goutam.razorpay.vault.controller;

import com.goutam.razorpay.vault.dto.response.TokenizeResponse;
import com.goutam.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vaults")
public class VaultController {

    private final VaultService vaultService;
    UUID merchantId = UUID.fromString("f3e1c2d4-5b6a-7c8d-9e0f-1a2b3c4d5e6f");

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@RequestBody TokenizeResponse request) {
        // Implement the logic to tokenize the card details and return a response
        // For now, returning a dummy response
        TokenizeResponse response = new TokenizeResponse(
                "dummy_token",
                "1234",
                null,
                12,
                2026
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vaultService.tokenize(request,merchantId));
    }
}
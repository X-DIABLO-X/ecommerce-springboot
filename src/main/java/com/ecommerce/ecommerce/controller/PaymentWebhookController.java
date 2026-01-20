package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

    private final PaymentService paymentService;

    @PostMapping("/razorpay")
    public ResponseEntity<Map<String, String>> handleRazorpayWebhook(@RequestBody Map<String, Object> webhookData) {
        log.info("Received Razorpay webhook");

        try {
            paymentService.handleRazorpayWebhook(webhookData);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Webhook processed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to process Razorpay webhook", e);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to process webhook: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}

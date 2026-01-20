package com.mockpayment.service;

import com.mockpayment.dto.CreatePaymentRequest;
import com.mockpayment.dto.WebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessorService {

    private final RestTemplate restTemplate;

    @Value("${ecommerce.webhook.url}")
    private String webhookUrl;

    @Async
    public void processPaymentAsync(CreatePaymentRequest request) {
        try {
            log.info("Processing payment for order: {}", request.getOrderId());
            
            // Simulate payment processing delay (3 seconds)
            TimeUnit.SECONDS.sleep(3);

            // Generate mock payment ID
            String mockPaymentId = "mock_pay_" + UUID.randomUUID().toString().substring(0, 8);

            // Simulate payment success (can be randomized for testing)
            String status = "SUCCESS"; // or "FAILED" for testing failure scenarios

            // Send webhook to e-commerce API
            WebhookRequest webhookRequest = new WebhookRequest();
            webhookRequest.setOrderId(request.getOrderId());
            webhookRequest.setStatus(status);
            webhookRequest.setPaymentId(mockPaymentId);

            log.info("Sending webhook to: {} for order: {}", webhookUrl, request.getOrderId());
            restTemplate.postForEntity(webhookUrl, webhookRequest, String.class);
            
            log.info("Webhook sent successfully for order: {}", request.getOrderId());
        } catch (InterruptedException e) {
            log.error("Payment processing interrupted for order: {}", request.getOrderId(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Failed to process payment for order: {}", request.getOrderId(), e);
        }
    }
}

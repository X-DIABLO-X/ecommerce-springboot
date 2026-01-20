package com.ecommerce.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockPaymentWebhookRequest {
    private String orderId;
    private String status; // SUCCESS or FAILED
    private String paymentId;
}

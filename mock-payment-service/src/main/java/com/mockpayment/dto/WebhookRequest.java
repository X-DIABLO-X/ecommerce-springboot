package com.mockpayment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {
    private String orderId;
    private String status;
    private String paymentId;
}

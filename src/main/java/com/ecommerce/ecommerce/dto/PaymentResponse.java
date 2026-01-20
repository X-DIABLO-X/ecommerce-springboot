package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String orderId;
    private Double amount;
    private PaymentStatus status;
    private String paymentId;
    private String razorpayOrderId;
    private Instant createdAt;
}

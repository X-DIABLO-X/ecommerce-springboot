package com.ecommerce.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    private String orderId;

    private Double amount;

    private PaymentStatus status;

    private String paymentId; // External payment ID (from Razorpay or Mock)

    private String razorpayOrderId; // Razorpay specific order ID

    private Instant createdAt;

    public Payment(String orderId, Double amount, PaymentStatus status) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.createdAt = Instant.now();
    }
}

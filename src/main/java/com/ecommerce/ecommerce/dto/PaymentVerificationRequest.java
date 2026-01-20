package com.ecommerce.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationRequest {
    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;
    private String orderId;
}

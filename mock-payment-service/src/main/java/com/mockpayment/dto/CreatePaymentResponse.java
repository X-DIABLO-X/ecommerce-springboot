package com.mockpayment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentResponse {
    private String message;
    private String orderId;
    private Double amount;
    private String paymentId;
}

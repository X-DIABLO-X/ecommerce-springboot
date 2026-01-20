package com.ecommerce.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {
    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private Double amount;
}

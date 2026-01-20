package com.ecommerce.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productId;
    private String productName;
    private Integer quantity;
    private Double price; // Price at the time of order
}

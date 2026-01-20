package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.model.OrderItem;
import com.ecommerce.ecommerce.model.OrderStatus;
import com.ecommerce.ecommerce.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String userId;
    private Double totalAmount;
    private OrderStatus status;
    private Instant createdAt;
    private List<OrderItem> items;
    private Payment payment;
}

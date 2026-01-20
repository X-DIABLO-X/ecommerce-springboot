package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.ecommerce.dto.OrderResponse;
import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable String userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
}

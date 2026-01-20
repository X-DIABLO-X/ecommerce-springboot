package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.ecommerce.dto.OrderResponse;
import com.ecommerce.ecommerce.model.*;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        log.info("Creating order for user: {}", request.getUserId());

        // 1. Fetch cart items
        List<CartItem> cartItems = cartService.getCartItemsByUserId(request.getUserId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Validate stock availability and calculate total
        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productService.getProductById(cartItem.getProductId());

            // Check stock availability
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                        ". Available: " + product.getStock() + ", Required: " + cartItem.getQuantity());
            }

            // Create order item with price snapshot
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            
            orderItems.add(orderItem);
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        // 3. Create order
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {} and total amount: {}", savedOrder.getId(), totalAmount);

        // 4. Update product stock
        for (CartItem cartItem : cartItems) {
            productService.updateStock(cartItem.getProductId(), -cartItem.getQuantity());
        }

        // 5. Clear cart
        cartService.clearCart(request.getUserId());
        log.info("Cart cleared after order creation");

        return savedOrder;
    }

    public OrderResponse getOrderById(String orderId) {
        log.info("Fetching order with ID: {}", orderId);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(order.getItems());

        // Fetch payment details if exists
        paymentRepository.findByOrderId(orderId).ifPresent(response::setPayment);

        return response;
    }

    public List<Order> getUserOrders(String userId) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus status) {
        log.info("Updating order {} status to {}", orderId, status);
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(status);
        orderRepository.save(order);
        
        log.info("Order status updated successfully");
    }
}

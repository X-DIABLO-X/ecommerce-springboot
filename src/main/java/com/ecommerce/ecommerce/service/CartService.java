package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.AddToCartRequest;
import com.ecommerce.ecommerce.dto.CartItemResponse;
import com.ecommerce.ecommerce.model.CartItem;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Transactional
    public CartItem addToCart(AddToCartRequest request) {
        log.info("Adding product {} to cart for user {}", request.getProductId(), request.getUserId());

        // Validate product exists
        Product product = productService.getProductById(request.getProductId());

        // Check if sufficient stock available
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
        }

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(
                request.getUserId(), request.getProductId());

        CartItem cartItem;
        if (existingItem.isPresent()) {
            // Update quantity
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
            }

            cartItem.setQuantity(newQuantity);
            log.info("Updated cart item quantity to {}", newQuantity);
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setUserId(request.getUserId());
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            log.info("Created new cart item");
        }

        return cartItemRepository.save(cartItem);
    }

    public List<CartItemResponse> getCartItems(String userId) {
        log.info("Fetching cart items for user: {}", userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemResponse> responses = new ArrayList<>();

        for (CartItem item : cartItems) {
            CartItemResponse response = new CartItemResponse();
            response.setId(item.getId());
            response.setUserId(item.getUserId());
            response.setProductId(item.getProductId());
            response.setQuantity(item.getQuantity());

            // Fetch product details
            try {
                Product product = productService.getProductById(item.getProductId());
                response.setProduct(product);
            } catch (Exception e) {
                log.warn("Product not found for cart item: {}", item.getProductId());
            }

            responses.add(response);
        }

        return responses;
    }

    public List<CartItem> getCartItemsByUserId(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public void clearCart(String userId) {
        log.info("Clearing cart for user: {}", userId);
        cartItemRepository.deleteByUserId(userId);
        log.info("Cart cleared successfully");
    }
}

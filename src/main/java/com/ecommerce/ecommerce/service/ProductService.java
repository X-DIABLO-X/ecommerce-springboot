package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CreateProductRequest;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest request) {
        log.info("Creating product: {}", request.getName());
        
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        
        return savedProduct;
    }

    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        log.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public List<Product> searchProducts(String query) {
        log.info("Searching products with query: {}", query);
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public void updateStock(String productId, Integer quantity) {
        log.info("Updating stock for product {} by {}", productId, quantity);
        
        Product product = getProductById(productId);
        int newStock = product.getStock() + quantity;
        
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        product.setStock(newStock);
        productRepository.save(product);
        
        log.info("Stock updated for product {}. New stock: {}", productId, newStock);
    }
}

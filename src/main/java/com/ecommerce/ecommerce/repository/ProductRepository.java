package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String name);
}

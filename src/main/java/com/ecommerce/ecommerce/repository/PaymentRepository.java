package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByPaymentId(String paymentId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}

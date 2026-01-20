package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CreatePaymentRequest;
import com.ecommerce.ecommerce.dto.PaymentResponse;
import com.ecommerce.ecommerce.dto.PaymentVerificationRequest;
import com.ecommerce.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        paymentService.verifyAndUpdatePayment(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment verified successfully");
        return ResponseEntity.ok(response);
    }
}

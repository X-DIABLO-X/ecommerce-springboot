package com.mockpayment.controller;

import com.mockpayment.dto.CreatePaymentRequest;
import com.mockpayment.dto.CreatePaymentResponse;
import com.mockpayment.service.PaymentProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentProcessorService paymentProcessorService;

    @PostMapping("/create")
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Received payment request for order: {}", request.getOrderId());

        // Trigger async payment processing
        paymentProcessorService.processPaymentAsync(request);

        CreatePaymentResponse response = new CreatePaymentResponse();
        response.setMessage("Payment processing initiated. Webhook will be sent in ~3 seconds.");
        response.setOrderId(request.getOrderId());
        response.setAmount(request.getAmount());
        response.setPaymentId(request.getPaymentId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Mock Payment Service is running");
    }
}

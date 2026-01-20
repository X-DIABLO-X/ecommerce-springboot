package com.ecommerce.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class RazorpayConfigController {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @GetMapping("/razorpay")
    public ResponseEntity<Map<String, String>> getRazorpayConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("keyId", razorpayKeyId);
        return ResponseEntity.ok(config);
    }
}

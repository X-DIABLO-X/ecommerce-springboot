package com.mockpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MockPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockPaymentServiceApplication.class, args);
    }
}

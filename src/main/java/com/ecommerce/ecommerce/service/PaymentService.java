package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CreatePaymentRequest;
import com.ecommerce.ecommerce.dto.PaymentResponse;
import com.ecommerce.ecommerce.dto.PaymentVerificationRequest;
import com.ecommerce.ecommerce.model.Order;
import com.ecommerce.ecommerce.model.OrderStatus;
import com.ecommerce.ecommerce.model.Payment;
import com.ecommerce.ecommerce.model.PaymentStatus;
import com.ecommerce.ecommerce.repository.OrderRepository;
import com.ecommerce.ecommerce.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("Creating Razorpay payment for order: {}", request.getOrderId());

        // Validate order exists and is in CREATED status
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Order is not in CREATED status. Current status: " + order.getStatus());
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(Instant.now());

        return createRazorpayPayment(payment);
    }

    private PaymentResponse createRazorpayPayment(Payment payment) {
        try {
            log.info("Creating Razorpay order for payment");

            // Create Razorpay order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (payment.getAmount() * 100)); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", payment.getOrderId());

            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            
            String razorpayOrderId = razorpayOrder.get("id");
            log.info("Razorpay order created: {}", razorpayOrderId);

            payment.setRazorpayOrderId(razorpayOrderId);
            Payment savedPayment = paymentRepository.save(payment);

            return mapToPaymentResponse(savedPayment);
        } catch (Exception e) {
            log.error("Failed to create Razorpay order", e);
            throw new RuntimeException("Failed to create Razorpay payment: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void handleRazorpayWebhook(Map<String, Object> webhookData) {
        log.info("Processing Razorpay webhook");

        try {
            String event = (String) webhookData.get("event");
            
            if ("payment.captured".equals(event)) {
                Map<String, Object> payload = (Map<String, Object>) webhookData.get("payload");
                Map<String, Object> paymentEntity = (Map<String, Object>) payload.get("payment");
                Map<String, Object> entity = paymentEntity != null ? paymentEntity : (Map<String, Object>) payload.get("payment.entity");

                String razorpayPaymentId = (String) entity.get("id");
                String razorpayOrderId = (String) entity.get("order_id");
                String status = (String) entity.get("status");

                log.info("Razorpay payment captured - PaymentID: {}, OrderID: {}, Status: {}", 
                        razorpayPaymentId, razorpayOrderId, status);

                // Find payment by razorpayOrderId
                Payment payment = paymentRepository.findAll().stream()
                        .filter(p -> razorpayOrderId.equals(p.getRazorpayOrderId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Payment not found for Razorpay order: " + razorpayOrderId));

                if ("captured".equals(status)) {
                    payment.setStatus(PaymentStatus.SUCCESS);
                    payment.setPaymentId(razorpayPaymentId);
                    
                    // Update order status to PAID
                    orderService.updateOrderStatus(payment.getOrderId(), OrderStatus.PAID);
                    log.info("Razorpay payment successful. Order marked as PAID");
                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                    orderService.updateOrderStatus(payment.getOrderId(), OrderStatus.FAILED);
                    log.info("Razorpay payment failed. Order marked as FAILED");
                }

                paymentRepository.save(payment);
            }
        } catch (Exception e) {
            log.error("Failed to process Razorpay webhook", e);
            throw new RuntimeException("Failed to process Razorpay webhook", e);
        }
    }

    @Transactional
    public void verifyAndUpdatePayment(PaymentVerificationRequest request) {
        log.info("Verifying payment for order: {}", request.getOrderId());

        try {
            // Verify signature
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", request.getRazorpay_order_id());
            options.put("razorpay_payment_id", request.getRazorpay_payment_id());
            options.put("razorpay_signature", request.getRazorpay_signature());

            boolean isValid = Utils.verifyPaymentSignature(options, razorpayKeySecret);

            if (isValid) {
                log.info("Payment signature verified successfully");

                // Update payment status
                Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpay_order_id())
                        .orElseThrow(() -> new RuntimeException("Payment not found"));

                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaymentId(request.getRazorpay_payment_id());
                paymentRepository.save(payment);

                // Update order status
                orderService.updateOrderStatus(request.getOrderId(), OrderStatus.PAID);
                log.info("Payment verified and order updated to PAID");
            } else {
                log.error("Payment signature verification failed");
                throw new RuntimeException("Invalid payment signature");
            }
        } catch (Exception e) {
            log.error("Error verifying payment", e);
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setPaymentId(payment.getPaymentId());
        response.setRazorpayOrderId(payment.getRazorpayOrderId());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}

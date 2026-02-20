package com.example.kafkaecommerce.controller;

import com.example.kafkaecommerce.model.Order;
import com.example.kafkaecommerce.producer.OrderProducer;
import com.example.kafkaecommerce.service.OrderIdempotencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderProducer orderProducer;
    private final OrderIdempotencyService idempotencyService; // ← ADD

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {

        // ← ADD: Idempotency check
        if (request.orderId() != null) {
            if (idempotencyService.isDuplicate(request.orderId())) {
                log.warn("⚠️ Duplicate order rejected: {}", request.orderId());
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Duplicate! OrderId: " + request.orderId() + " already processed!");
            }
        }

        // Order object create karo
        Order order = Order.create(
                request.orderId(),   // ← CHANGED
                request.customerId(),
                request.productName(),
                request.quantity(),
                request.totalAmount()
        );

        // Kafka mein bhejo
        orderProducer.sendOrder(order);

        // ← ADD: Redis mein store karo
        idempotencyService.markAsProcessed(order.getOrderId());

        log.info("📦 Order placed: {}", order.getOrderId());
        return ResponseEntity.ok(order);
    }

    // ← CHANGED: orderId add kiya
    public record OrderRequest(
            String orderId,       // nullable - client bhejega
            String customerId,
            String productName,
            int quantity,
            double totalAmount
    ) {}
}
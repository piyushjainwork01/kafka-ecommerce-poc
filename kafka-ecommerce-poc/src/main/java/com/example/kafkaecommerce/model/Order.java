package com.example.kafkaecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String orderId;
    private String customerId;
    private String productName;
    private int quantity;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public static Order create(String orderId,      // ← orderId PEHLE
                               String customerId,   // ← customerId BAAD
                               String productName,
                               int quantity,
                               double totalAmount) {
        Order order = new Order();
        order.setOrderId(orderId != null ? orderId : UUID.randomUUID().toString());
        order.setCustomerId(customerId);
        order.setProductName(productName);
        order.setQuantity(quantity);
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }
}
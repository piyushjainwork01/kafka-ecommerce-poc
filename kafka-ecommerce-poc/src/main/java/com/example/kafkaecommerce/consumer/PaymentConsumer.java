package com.example.kafkaecommerce.consumer;

import com.example.kafkaecommerce.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentConsumer {

    @KafkaListener(
            topics = "orders",
            groupId = "payment-group"
    )
    public void processPayment(Order order) {

        log.info("💳 Payment Service received order:" +
                        "\n   OrderId: {}" +
                        "\n   Customer: {}" +
                        "\n   Product: {}" +
                        "\n   Amount: ₹{}",
                order.getOrderId(),
                order.getCustomerId(),
                order.getProductName(),
                order.getTotalAmount()
        );

        // Simulate payment processing
        if (order.getTotalAmount() > 0) {
            log.info("✅ Payment successful for OrderId: {}", order.getOrderId());
        } else {
            log.error("❌ Payment failed for OrderId: {}", order.getOrderId());
        }
    }
}
/* TODO: ### Theory Connection 🔗
```
@KafkaListener → ye annotation consumer banata hai
topics = "orders" → "orders" topic se padho
groupId = "payment-group" → ye apna consumer group hai
```

Yaad hai theory mein padha tha?
```
Multiple Consumer Groups → same topic independently padhte hain
payment-group    → apna offset track karta hai
notification-group → apna offset track karta hai (next step)*/
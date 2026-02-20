package com.example.kafkaecommerce.producer;


import com.example.kafkaecommerce.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {

    private static final String TOPIC = "orders";

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public void sendOrder(Order order) {
        kafkaTemplate.send(TOPIC, order.getCustomerId(), order)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Order sent successfully!" +
                                        "\n   OrderId: {}" +
                                        "\n   Topic: {}" +
                                        "\n   Partition: {}" +
                                        "\n   Offset: {}",
                                order.getOrderId(),
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    } else {
                        log.error("❌ Failed to send order: {}", ex.getMessage());
                    }
                });
    }
}
//```
//
//        ---
//
//        ### Theory Connection 🔗
//        ```
//TOPIC    → "orders"         // Step 2 mein padha tha
//KEY      → order.getOrderId() // Step 3 mein padha tha (same key = same partition)
//VALUE    → order            // Order object JSON mein convert hoga
//Partition→ log mein dikhega // Kafka ne decide kiya
//Offset   → log mein dikhega // Position number in partition
package com.example.kafkaecommerce.consumer;

import com.example.kafkaecommerce.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @KafkaListener(
            topics = "orders",
            groupId = "notification-group"
    )
    public void sendNotification(Order order) {

        log.info("🔔 Notification Service received order:" +
                        "\n   OrderId: {}" +
                        "\n   Customer: {}" +
                        "\n   Product: {}",
                order.getOrderId(),
                order.getCustomerId(),
                order.getProductName()
        );

        // Simulate notification
        log.info("📧 Email sent to Customer: {} → Your order {} is confirmed!",
                order.getCustomerId(),
                order.getOrderId()
        );
    }
}

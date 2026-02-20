package com.example.kafkaecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderIdempotencyService {

    private final RedisTemplate<String, String> redisTemplate;

    // 24 hours tak store rahega
    private static final Duration TTL = Duration.ofHours(24);
    private static final String PREFIX = "order:";

    public boolean isDuplicate(String orderId) {
        Boolean exists = redisTemplate.hasKey(PREFIX + orderId);
        return Boolean.TRUE.equals(exists);
    }

    public void markAsProcessed(String orderId) {
        redisTemplate.opsForValue().set(PREFIX + orderId, "processed", TTL);
        log.info("✅ Order stored in Redis: {} (TTL: 24hrs)", orderId);
    }
}

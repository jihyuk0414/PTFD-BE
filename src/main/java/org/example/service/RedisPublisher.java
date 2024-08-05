package org.example.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {
    @Resource(name = "chatRoomRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String topic, String message) {
        log.info("published topic = {}", topic);
        redisTemplate.convertAndSend(topic, message);
    }
}

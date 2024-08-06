package org.example.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {
    @Resource(name = "chatRoomRedisTemplate")
    private final RedisTemplate redisTemplate;

    public void publish(ChannelTopic topic, Message message) {
        log.info("published topic = {}", topic.getTopic());

        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}

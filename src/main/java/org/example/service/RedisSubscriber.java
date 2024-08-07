package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Chatting;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber  implements MessageListener {

    private final ObjectMapper objectMapper;
    @Resource(name = "chatRoomRedisTemplate")
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            log.info("Received message: {}", publishMessage);
            log.info(publishMessage);
            Chatting roomMessage = objectMapper.readValue(publishMessage, Chatting.class);

            log.info("Deserialized message: {}", roomMessage.getContent());
            log.info(roomMessage.getRoomId());
            messagingTemplate.convertAndSend("/sub/room"+roomMessage.getRoomId() , roomMessage.getContent());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageReq;
import org.example.dto.MessageRes;
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

            MessageRes roomMessage= objectMapper.readValue(publishMessage, MessageRes.class);

            messagingTemplate.convertAndSend("/sub/room"+ roomMessage.getRoomId() , roomMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

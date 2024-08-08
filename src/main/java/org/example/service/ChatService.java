package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Message;
import org.example.repository.ChatRepository;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;
    private final ChatRepository chatRepository;

    public void pubMsgChannel(String channel ,Message message) {
        message.setSendAt(LocalDateTime.now().toString());
        redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic("room"+channel));
        redisPublisher.publish(new ChannelTopic("room"+channel), message);
        chatRepository.save(Message.toEntity(message));
    }
}

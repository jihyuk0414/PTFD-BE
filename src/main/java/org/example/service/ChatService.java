package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Message;
import org.example.entity.Chatting;
import org.example.repository.ChatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {


    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;
    private final ChatRepository chatRepository;


    public void pubMsgChannel(String channel ,Message message) {
        //1. 요청한 Channel 을 구독.
        log.info("구독");
        redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic(channel));
        //2. Message 전송
        log.info("전송");
        redisPublisher.publish(new ChannelTopic(channel), message.getContent());
        log.info("저장");
        chatRepository.save(Message.toEntity(message));
    }

    public void saveChat(Chatting chatting){
        chatRepository.save(chatting);
    }
    public List<Message> getChat(String room){
        return chatRepository.findByRoomId(room).stream().map(Chatting::toDto).toList();
    }
}

package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.example.dto.ChatMember;
import org.example.dto.MessageReq;
import org.example.dto.MessageRes;
import org.example.entity.Chatting;
import org.example.repository.ChatRepository;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final MemberFeign memberfeign;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;
    private final ChatRepository chatRepository;

    public void pubMsgChannel(String channel , MessageReq messageReq) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        ChatMember member=memberfeign.getProfile(messageReq.getSender());
        MessageRes res =MessageRes.builder()
                .sendAt(now.format(formatter))
                .content(messageReq.getContent())
                .roomId(channel)
                .sender(member)
                .build();
        redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic("room"+channel));
        redisPublisher.publish(new ChannelTopic("room"+channel), res);
        Chatting chat=chatRepository.save(MessageRes.toEntity(res));
        log.info(chat.toString());
    }
}

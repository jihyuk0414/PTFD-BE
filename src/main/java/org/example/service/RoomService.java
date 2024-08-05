package org.example.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.dto.RoomDto;
import org.example.entity.ChatRoom;
import org.example.repository.CustomRoomRepository;
import org.example.repository.RoomRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {

    @Resource(name = "chatRoomRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;
    private final CustomRoomRepository customRoomRepository;
    @Resource(name = "redisMessage")
    private final RedisMessageListenerContainer redisMessage;
    private final RedisSubscriber redisSubscriber;

    public String createRoom(RoomDto roomDto,String email){
        List<String> users = new ArrayList<>();
        users.add(email);
        ChatRoom chatRoom=roomRepository.save(ChatRoom.builder().room(roomDto.getRoom_id()).roomName(roomDto.getRoomName()).users(users).userCount(1).build());
        return chatRoom.getRoomName()+" 채팅방 생성";
    }

    public List<RoomDto> getChatRoom(String email){
        return roomRepository.findByUsersContaining(email).stream().map(ChatRoom::toDto).toList();
    }

    public void insertUser(String roomId,String email){
        List<String> users=roomRepository.findUsersByRoom(roomId);
        int userCount = roomRepository.findUserCountByRoom(roomId);
        users.add(email);
        customRoomRepository.updateUsers(roomId,users,userCount);
        redisTemplate.opsForSet().add("chatroom:" + roomId, email);
    }
    public void enterMessageRoom(String roomId) {
        ChannelTopic topic = new ChannelTopic(roomId);
        redisMessage.addMessageListener(redisSubscriber, topic);

    }
}

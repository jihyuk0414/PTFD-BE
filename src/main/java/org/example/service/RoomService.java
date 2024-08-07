package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ChatRoomMessage;
import org.example.dto.Message;
import org.example.dto.RoomDto;
import org.example.entity.ChatRoom;
import org.example.entity.Chatting;
import org.example.repository.ChatRepository;
import org.example.repository.CustomRoomRepository;
import org.example.repository.RoomRepository;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final CustomRoomRepository customRoomRepository;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public String createRoom(RoomDto roomDto,String email){
        List<String> users = new ArrayList<>();
        users.add(email);
        ChatRoom chatRoom=roomRepository.save(ChatRoom.builder().room(roomDto.getRoomId()).roomName(roomDto.getRoomName()).users(users).userCount(1).build());

        return chatRoom.getRoomName()+" 채팅방 생성";
    }
    public List<RoomDto> getChatRooms(String email){

        return roomRepository.findByUsersContaining(email).stream().map(ChatRoom::toDto).toList();
    }

    public ChatRoomMessage insertUser(String roomId, String email){
        ChatRoom room = roomRepository.findByRoom(roomId);
        log.info(room.getRoom());
        if(!room.getUsers().contains(email)){

            log.info(String.valueOf(room.getUserCount()));
            room.getUsers().add(email);
            customRoomRepository.updateUsers(roomId,room.getUsers(),room.getUserCount());
            redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic("room"+roomId));
        }
        ChatRoom room1=roomRepository.findByRoom(roomId);
        List<Message> chats = chatRepository.findByRoomId(roomId).stream().map(Chatting::toDto).toList();
        return ChatRoomMessage.builder()
                .chats(chats)
                .room_id(room1.getRoom())
                .roomName(room1.getRoomName())
                .userCount(room1.getUserCount())
                .build();
    }

}

package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ChatRoomMessage;
import org.example.dto.MessageReq;
import org.example.dto.MessageRes;
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
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final CustomRoomRepository customRoomRepository;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final MemberFeign memberFeign;

    public String createRoom(RoomDto roomDto,String email){
        List<String> users = new ArrayList<>();
        Optional<String> nickName= memberFeign.getNickName(email);
        users.add(nickName.get());
        ChatRoom chatRoom=roomRepository.save(ChatRoom.builder().room(roomDto.getRoomId()).roomName(roomDto.getRoomName()).users(users).userCount(1).build());
        return chatRoom.getRoomName()+" 채팅방 생성";
    }

    public List<RoomDto> getChatRooms(String email){
        Optional<String> nickName= memberFeign.getNickName(email);
        return roomRepository.findByUsersContaining(nickName.get()).stream().map(ChatRoom::toDto).toList();
    }

    public ChatRoomMessage insertUser(String roomId, String email){
        ChatRoom room = roomRepository.findByRoom(roomId);
        Optional<String> nickName= memberFeign.getNickName(email);
        if(!room.getUsers().contains(nickName.get())){
            room.getUsers().add(nickName.get());
            customRoomRepository.updateUsers(roomId,room.getUsers(),room.getUserCount());
            redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic("room"+roomId));
        }
        ChatRoom room1=roomRepository.findByRoom(roomId);
        List<MessageRes> chats = chatRepository.findByRoomId(roomId).stream().map(Chatting::toDto).toList();
        return ChatRoomMessage.builder()
                .chats(chats)
                .room_id(room1.getRoom())
                .roomName(room1.getRoomName())
                .userCount(room1.getUserCount())
                .build();
    }

}

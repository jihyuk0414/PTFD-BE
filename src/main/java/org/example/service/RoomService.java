package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;
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
    private final PostFeign postFeign;

    public String createRoomPost(String postId,String email){
        List<String> users = new ArrayList<>();
        if(roomRepository.existsByRoom(postId)){
            return "채팅방이 있습니다.";
        }
        PostForChat post = postFeign.getPostInfo(postId);
        Optional<String> nickName = memberFeign.getNickName(email);
        users.add(post.getNickName());
        users.add(nickName.get());
        ChatRoom chatRoom=roomRepository.save(ChatRoom.builder().room(postId).roomName(post.getPostName()).users(users).post(post).build());

        return chatRoom.getRoomName()+" 채팅방 생성";
    }

    public String createRoom(String nickName,String email){
        List<String> users = new ArrayList<>();
        Optional<String> nickName1 = memberFeign.getNickName(email);
        if(roomRepository.existsByRoom(nickName1.get()+","+nickName)){
            return "채팅방이 있습니다.";
        }
        users.add(nickName);
        users.add(nickName1.get());
        ChatRoom chatRoom = roomRepository.save(ChatRoom.builder().room(nickName1.get()+","+nickName).roomName(nickName1.get()+","+nickName).users(users).build());
        return chatRoom.getRoomName()+" 채팅방 생성";
    }

    public List<ChattingRoomRes> getChatRooms(String email){
        Optional<String> nickName= memberFeign.getNickName(email);
        List<ChatRoom> rooms =roomRepository.findByUsersContaining(nickName.get());
        List<ChattingRoomRes> res = new ArrayList<>();
        for (ChatRoom room : rooms){
            ChattingRoomRes roomRes;
            if (room.getPost()==null){

                String other = room.getUsers().stream().filter(u->!u.equals(nickName.get())).findFirst().get();
                String profile = memberFeign.getProfile(other).getProfile_image();
                roomRes = ChatRoom.toDto(room,other,profile);
            }
            else {
                roomRes = ChatRoom.toDtoPost(room);
            }
            Optional<Chatting> last = chatRepository.findFirstByRoomIdOrderBySendAtDesc(room.getRoom());
            last.ifPresent(chatting -> roomRes.setLastMsg(chatting.getContent()));
            res.add(roomRes);
        }
        return res;
    }

    public ChatRoomMessage insertUser(String roomId, String email){
        ChatRoom room1=roomRepository.findByRoom(roomId);
        List<MessageRes> chats = chatRepository.findByRoomId(roomId).stream().map(Chatting::toDto).toList();
        return ChatRoomMessage.builder()
                .chats(chats)
                .room_id(room1.getRoom())
                .roomName(room1.getRoomName())
                .build();
    }

}

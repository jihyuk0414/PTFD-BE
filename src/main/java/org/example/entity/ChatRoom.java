package org.example.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.example.dto.PostForChat;
import org.example.dto.RoomDto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="room")
@Getter
public class ChatRoom {
    @Id
    private String id;
    private String room;
    private String roomName;
    private PostForChat post;
    private List<String> users;

    @Builder
    public ChatRoom(String roomName,String room,List<String> users,PostForChat post){
        this.roomName=roomName;
        this.room=room;
        this.users=users;
        this.post=post;
    }

    public static RoomDto toDto(ChatRoom chatRoom){
        return RoomDto.builder()
                .room_id(chatRoom.getRoom())
                .roomName(chatRoom.getRoomName())

                .build();
    }
}

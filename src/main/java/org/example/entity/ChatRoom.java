package org.example.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.example.dto.RoomDto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="room")
@Getter
public class ChatRoom {
    @Id
    private String room;
    private String roomName;
    private int userCount;
    private List<String> users;

    @Builder
    public ChatRoom(String roomName,String room,int userCount,List<String> users){
        this.roomName=roomName;
        this.room=room;
        this.userCount=userCount;
        this.users=users;
    }

    public static RoomDto toDto(ChatRoom chatRoom){
        return RoomDto.builder()
                .room_id(chatRoom.getRoom())
                .roomName(chatRoom.getRoomName())
                .userCount(chatRoom.getUserCount())
                .build();
    }
}

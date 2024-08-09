package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.util.List;

@Data
@RequiredArgsConstructor
public class ChatRoomMessage {
    private String roomName;
    private String roomId;
    private int userCount;
    private List<MessageReq> chats;
    @Builder
    public ChatRoomMessage(String roomName,String room_id,int userCount,List<MessageReq> chats){
        this.roomName=roomName;
        this.roomId=room_id;
        this.userCount=userCount;
        this.chats=chats;
    }
}

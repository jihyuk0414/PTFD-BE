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
    private List<MessageRes> chats;

    @Builder
    public ChatRoomMessage(String roomName,String room_id,List<MessageRes> chats){
        this.roomName=roomName;
        this.roomId=room_id;
        this.chats=chats;
    }
}

package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.entity.Chatting;

@Data
@RequiredArgsConstructor
public class MessageRes {
    private String content;
    private ChatMember sender;
    private String sendAt;
    private String roomId;

    @Builder
    public MessageRes(String content, ChatMember sender, String sendAt,String roomId){
        this.content=content;
        this.sender=sender;
        this.sendAt=sendAt;
        this.roomId=roomId;
    }

    public static Chatting toEntity(MessageRes messageRes){
        return Chatting.builder()
                .content(messageRes.getContent())
                .roomId(messageRes.getRoomId())
                .sendAt(messageRes.getSendAt())
                .sender(messageRes.getSender())
                .build();
    }


}

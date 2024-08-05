package org.example.dto;

import lombok.*;
import org.example.entity.Chatting;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String sender;
    private String content;
    private MessageType type; // ENTER,TALK
    private String chatRoomId;

    public static Chatting toEntity(Message message){
        return Chatting.builder()
                .roomId(message.getChatRoomId())
                .content(message.getContent())
                .senderName(message.getSender())
                .build();
    }
}

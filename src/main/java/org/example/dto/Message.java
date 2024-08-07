package org.example.dto;

import lombok.*;
import org.example.entity.Chatting;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String sender;
    private String content;
    private String type;
    private String roomId;
    private LocalDateTime sendAt;

    public static Chatting toEntity(Message message){
        return Chatting.builder()
                .roomId(message.getRoomId())
                .content(message.getContent())
                .senderName(message.getSender())
                .sendAt(message.getSendAt())
                .build();
    }
}

package org.example.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.*;
import org.example.dto.Message;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Document(collection="chatting")
@Getter
@ToString

@RequiredArgsConstructor
public class Chatting {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String content;
    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sendAt;

    @Builder
    public Chatting( String roomId, String senderName, String content, String type, LocalDateTime sendAt) {
        this.roomId=roomId;
        this.sender = senderName;
        this.content = content;
        this.type=type;
        this.sendAt=sendAt;
    }

    public static Message toDto(Chatting chatting){
        return Message.builder()
                .sender(chatting.getSender())
                .content(chatting.getContent())
                .roomId(chatting.getRoomId())
                .sendAt(chatting.getSendAt())
                .build();
    }
}

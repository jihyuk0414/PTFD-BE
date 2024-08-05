package org.example.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.example.dto.Message;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection="chatting")
@Getter
@ToString
@RequiredArgsConstructor
public class Chatting {
    @Id
    private String id;

    private String roomId;
    private String senderName;
    private String content;
    private LocalDateTime sendDate;
    private long readCount;

    @Builder
    public Chatting(String id, String roomId, String senderName, String content, LocalDateTime sendDate, long readCount) {
        this.id = id;
        this.roomId=roomId;
        this.senderName = senderName;
        this.content = content;
        this.sendDate = sendDate;
        this.readCount = readCount;
    }
    public static Message toDto(Chatting chatting){
        return Message.builder()
                .sender(chatting.getSenderName())
                .content(chatting.getContent())
                .chatRoomId(chatting.getRoomId())
                .build();
    }
}

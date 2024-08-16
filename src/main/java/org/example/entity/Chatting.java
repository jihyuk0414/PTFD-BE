package org.example.entity;


import jakarta.persistence.Id;
import lombok.*;
import org.example.dto.ChatMember;
import org.example.dto.MessageReq;
import org.example.dto.MessageRes;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="chatting")
@Getter
@ToString
@RequiredArgsConstructor
public class Chatting {
    @Id
    private String id;
    private String roomId;
    private ChatMember sender;
    private String content;
    private String sendAt;

    @Builder
    public Chatting( String roomId, ChatMember sender, String content, String sendAt) {
        this.roomId=roomId;
        this.sender=sender;
        this.content = content;
        this.sendAt=sendAt;
    }

    public static MessageRes toDto(Chatting chatting){
        return MessageRes.builder()
                .sender(chatting.getSender())
                .content(chatting.getContent())
                .roomId(chatting.getRoomId())
                .build();
    }
}

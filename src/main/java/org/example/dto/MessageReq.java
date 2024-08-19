package org.example.dto;


import lombok.*;


import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageReq implements Serializable {
    private String sender;
    private String content;
    private String roomId;
}

package org.example.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomDto {
    private String roomName;
    private String roomId;
    private PostForChat post;


    @Builder
    public RoomDto(String roomName,String room_id,PostForChat post){
        this.roomName=roomName;
        this.roomId=room_id;
        this.post=post;
    }
}

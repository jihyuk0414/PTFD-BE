package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChattingRoomRes {
    private String roomName;
    private String roomId;
    private PostForChat post;
    private String nickName;
    private String userProfile;
    private String lastMsg;

    @Builder
    public ChattingRoomRes(String roomName,String room_id,PostForChat post,String nickName, String userProfile,String lastMsg){
        this.roomName=roomName;
        this.roomId=room_id;
        this.post=post;
        this.nickName=nickName;
        this.userProfile=userProfile;
        this.lastMsg=lastMsg;
    }
}

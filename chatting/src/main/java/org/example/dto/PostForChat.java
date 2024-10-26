package org.example.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@RequiredArgsConstructor
public class PostForChat {
    private String postName;
    private int price;
    private String imagePost;
    private String nickName;
    private String userProfile;
    private String postInfo;

    @Builder
    public PostForChat(String postName,int price,String imagePost,String nickName,String userProfile,String postInfo){
        this.imagePost=imagePost;
        this.postInfo=postInfo;
        this.price=price;
        this.postName=postName;
        this.nickName=nickName;
        this.userProfile=userProfile;
    }
}

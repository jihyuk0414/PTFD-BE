package org.example.dto.post;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostForMessage {
    private String post_name;
    private String image_post;

    @Builder
    public PostForMessage(String postName,String image_post){
        this.image_post=image_post;
        this.post_name=postName;
    }
}

package org.example.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MessageRes {
    @JsonProperty("post_name")
    private String post_name;

    @JsonProperty("image_real")
    private String image_real;

    @Builder
    public MessageRes(String postName, String image_real){
        this.image_real=image_real;
        this.post_name=postName;
    }
}

package org.example.dto.post;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostFeignReq {
    private String email;
    private List<Long> post_id;

    @Builder
    public PostFeignReq(String email, List<Long> post_id){
        this.email=email;
        this.post_id=post_id;
    }
}

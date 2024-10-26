package org.example.dto.post;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostFeignRes {
    private boolean success;
    private int soldOutIds;

    @Builder
    public PostFeignRes(boolean success, int soldOutIds){
        this.soldOutIds=soldOutIds;
        this.success=success;
    }
}

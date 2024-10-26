package org.example.dto.wish_list;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.entity.Post;

import java.util.List;

@Data
@RequiredArgsConstructor
public class WishListDto {
    private String message;
    private List<Post> likePosts;
    @Builder
    public WishListDto(String message, List<Post> likePosts){
        this.likePosts=likePosts;
        this.message=message;
    }
}

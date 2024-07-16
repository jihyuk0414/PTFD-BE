package org.example.dto.post;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.entity.Post;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostDetailRes {
    private Post post;
    private List<Post> postList;
    private boolean me;
    @Builder
    public PostDetailRes(Post post, List<Post> postList,boolean me){
        this.post=post;
        this.postList=postList;
        this.me=me;
    }
}

package org.example.service;

import org.example.dto.PostForChat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "post",url = "http://post-service:70/post")
public interface PostFeign {
    @GetMapping("/chat")
    public PostForChat getPostInfo(@RequestParam("post_id") String postId);
}

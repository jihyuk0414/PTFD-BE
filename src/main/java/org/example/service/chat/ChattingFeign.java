package org.example.service.chat;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "chatApi",url = "http://chat-service:50/chatroom")
public interface ChattingFeign {

    @PostMapping("/post")
    public boolean changePostInfo(
            @RequestParam("before_post_name") String before_post_name,
            @RequestParam("new_post_name") String new_post_name,
                                  @RequestParam("price") int new_price,
                                  @RequestParam("image_post") String new_post_img,
                                  @RequestParam("post_info") String new_post_info);
}

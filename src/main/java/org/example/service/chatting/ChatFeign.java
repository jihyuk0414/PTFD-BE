package org.example.service.chatting;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "chatApi",url = "http://chat-service:50/chatroom")
public interface ChatFeign {

    @PostMapping("/nick_name")
    public void changeNickName(@RequestParam("new_nick_name") String new_nick_name,
                               @RequestParam("before_nick_name") String before_nick_name);

}

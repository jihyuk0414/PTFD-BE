package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageReq;
import org.example.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(@RequestBody MessageReq messageReq) {
        chatService.pubMsgChannel(messageReq.getRoomId(), messageReq);
    }

    @PostMapping("/chat/nick_name")
    public void changeNickName(@RequestParam("new_nick_name") String new_nick_name,
                                 @RequestParam("before_nick_name") String before_nick_name)
    {
        //nickname before인거를 new로 바꿔주기
        log.info("chat으로 요청 잘옴"+new_nick_name+"새거고"+before_nick_name) ;

    };
}

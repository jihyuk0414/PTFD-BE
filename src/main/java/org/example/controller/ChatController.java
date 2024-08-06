package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Message;
import org.example.service.ChatService;
import org.example.service.RedisPublisher;
import org.example.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final RoomService roomService;

    @MessageMapping("/chat/message")
    public void message(@RequestBody Message message) {

        chatService.pubMsgChannel(message.getRoomId(), message);
    }










}

package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Message;
import org.example.service.ChatService;
import org.example.service.RedisPublisher;
import org.example.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final RoomService roomService;

    @MessageMapping("/chat/message")
    public void message(@RequestBody Message message) {
        log.info("메시지 동작");
        message.setSender("email");
        chatService.pubMsgChannel(message.getChatRoomId(), message);
    }

    @GetMapping("/chat/{roomId}")
    public List<Message> getAllMessage(@PathVariable("roomId")String roomId){
        return chatService.getChat(roomId);
    }








}

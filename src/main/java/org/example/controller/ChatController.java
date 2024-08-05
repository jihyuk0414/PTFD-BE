package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Message;
import org.example.service.ChatService;
import org.example.service.RedisPublisher;
import org.example.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/chat")
public class ChatController {
    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final RoomService roomService;

    @MessageMapping("/chat/message/{email}")
    public void message(Message message, @PathVariable("email") String email) {
        message.setSender(email);
        roomService.enterMessageRoom(message.getChatRoomId());
        chatService.saveChat(Message.toEntity(message));
        redisPublisher.publish("chatroom:"+message.getChatRoomId(),message.getContent()); //RedisPublisher 호출
    }

    @GetMapping("/room/{roomId}")
    public List<Message> getAllMessage(@PathVariable("roomId")String roomId){
        return chatService.getChat(roomId);
    }








}

package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChatRoomMessage;
import org.example.dto.ChattingRoomRes;
import org.example.dto.RoomDto;
import org.example.service.RoomService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("chatroom")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/make/post/{post_id}/{email}")
    public String makeRoomPost(@PathVariable("email") String email,@PathVariable(value = "post_id") String postId){
        return roomService.createRoomPost(postId,email);
    }
    @PostMapping("/make/{nick_name}/{email}")
    public String makeRoom(@PathVariable("email") String email,@PathVariable(value = "nick_name") String nickName){
        return roomService.createRoom(nickName,email);
    }

    @GetMapping("/search/{email}")
    public List<ChattingRoomRes> getRooms(@PathVariable("email") String email){
        return roomService.getChatRooms(email);
    }

    @PostMapping("/enter/{room_id}/{email}")
    public ChatRoomMessage enterRoom(@PathVariable("room_id") String roomId, @PathVariable("email") String email){
        return roomService.insertUser(roomId,email);
    }

}

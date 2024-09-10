package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ChatRoomMessage;
import org.example.dto.ChattingRoomRes;
import org.example.dto.RoomDto;
import org.example.service.RoomService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("chatroom")
@Slf4j
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

    @GetMapping("/enter/{room_id}/{email}")
    public ChatRoomMessage enterRoom(@PathVariable("room_id") String roomId, @PathVariable("email") String email){
        return roomService.insertUser(roomId,email);
    }
    @PostMapping("/nick_name")
    public void changeNickName(@RequestParam("new_nick_name") String new_nick_name,
                               @RequestParam("before_nick_name") String before_nick_name)
    {
        //nickname before인거를 new로 바꿔주기
        log.info("chat으로 요청 잘옴"+new_nick_name+"새거고"+before_nick_name) ;

    };

}

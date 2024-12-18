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
    public boolean changeNickName(@RequestParam("new_nick_name") String new_nick_name,
                               @RequestParam("before_nick_name") String before_nick_name,
                                  @RequestParam("new_profile_img") String new_profile_img)
    {

        return roomService.updateUserNickName(new_nick_name, before_nick_name,new_profile_img);
    };

    @PostMapping("/post")
    public boolean changePostInfo(
            @RequestParam("before_post_name") String before_post_name,
            @RequestParam("new_post_name") String new_post_name,
                                  @RequestParam("price") int new_price,
                                  @RequestParam("image_post") String new_post_img,
                                  @RequestParam("post_info") String new_post_info)
    {
        return roomService.updatePostInfo(before_post_name, new_post_name, new_price, new_post_img, new_post_info);
    }

}

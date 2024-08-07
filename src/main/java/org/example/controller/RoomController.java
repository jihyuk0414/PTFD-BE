package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChatRoomMessage;
import org.example.dto.RoomDto;
import org.example.service.RoomService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("chatroom")
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/make/{email}")
    public String makeRoom(@RequestBody RoomDto roomDto,@PathVariable("email") String email){
        return roomService.createRoom(roomDto,email);
    }

    @GetMapping("/search/{email}")
    public List<RoomDto> getRooms(@PathVariable("email") String email){
        return roomService.getChatRooms(email);
    }

    @PostMapping("/enter/{room_id}/{email}")
    public ChatRoomMessage enterRoom(@PathVariable("room_id") String roomId, @PathVariable("email") String email){
        return roomService.insertUser(roomId,email);
    }

}

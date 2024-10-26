package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.follow.FollowerDto;
import org.example.dto.follow.FollowingDto;
import org.example.dto.follow.FollowDto;
import org.example.service.FollowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{email}")
    public String follow(@PathVariable("email") String email ,@RequestBody FollowDto followDto){
        return  followService.FollowReq(followDto.getEmail(),email);
    }

    @GetMapping("/follower/{nick_name}")
    public FollowerDto follower(@PathVariable("nick_name") String nickName){
        return followService.getFollower(nickName);
    }

    @GetMapping("/following/{nick_name}")
    public FollowingDto following(@PathVariable("nick_name") String nickName){
        return followService.getFollowing(nickName);
    }

}

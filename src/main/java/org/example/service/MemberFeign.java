package org.example.service;

import org.example.dto.ChatMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Component
@FeignClient(name = "member",url = "http://member-service:81/member")
public interface MemberFeign {
    @GetMapping("/nick_name")
    public Optional<String> getNickName(@RequestParam("email") String email);
    @GetMapping("/info")
    public ChatMember getProfile(@RequestParam("nick_name") String nick_name);
}

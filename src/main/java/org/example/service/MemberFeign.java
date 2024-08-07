package org.example.service;

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
}

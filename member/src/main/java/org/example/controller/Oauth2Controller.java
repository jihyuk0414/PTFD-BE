package org.example.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.ParseException;
import org.example.jwt.JwtDto;
import org.example.service.MemberService;
import org.example.service.kakao.KakaoService;
import org.example.service.naver.NaverService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2Controller {
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final MemberService memberService;
    @GetMapping("/oauth2/kakao")
    public JwtDto kakaoToken(@RequestParam("code") String code,@RequestParam("role") String role) throws IOException, ParseException, org.json.simple.parser.ParseException {
        log.info("전달받은 role"+ role);
        return kakaoService.GenerateToken(code,role);
    }

    @GetMapping("/oauth2/naver")
    public JwtDto naverToken(@RequestParam("code") String code,@RequestParam("role") String role) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return naverService.GenerateToken(code,role);
    }
    @PostMapping("/kakao/logout")
    public String kakaoLogOut(){
        return kakaoService.kakaoLogOut();
    }







}

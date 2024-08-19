package org.example.controller;


import lombok.RequiredArgsConstructor;
import net.minidev.json.parser.ParseException;
import org.example.jwt.JwtDto;
import org.example.service.MemberService;
import org.example.service.kakao.KakaoService;
import org.example.service.naver.NaverService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor

public class Oauth2Controller {
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final MemberService memberService;
    @GetMapping("/oauth2/kakao")
    public JwtDto kakaoToken(@RequestParam("code") String code) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return kakaoService.GenerateToken(code);
    }

    @GetMapping("/oauth2/naver")
    public JwtDto naverToken(@RequestParam("code") String code) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return naverService.GenerateToken(code);
    }
    @PostMapping("/kakao/logout")
    public String kakaoLogOut(){
        return kakaoService.kakaoLogOut();
    }







}

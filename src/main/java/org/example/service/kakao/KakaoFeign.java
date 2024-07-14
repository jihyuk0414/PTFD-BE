package org.example.service.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "kakao",url = "https://kauth.kakao.com/oauth")
public interface KakaoFeign {
    @PostMapping("/token")
    public String getAccessToken(@RequestHeader("Content-type") String Content,
                                 @RequestParam("grant_type") String grant,
                                 @RequestParam("client_id") String client,
                                 @RequestParam("redirect_uri") String redirect,
                                 @RequestParam("code") String code,
                                 @RequestParam("client_secret") String secret);
    //https://kapi.kakao.com/v1/user/logout
    //  -H "Content-Type: application/x-www-form-urlencoded" \
    //  -H "Authorization: Bearer ${ACCESS_TOKEN}"
    @GetMapping("/logout")
    public String logOut(@RequestParam("client_id") String client_id,@RequestParam("logout_redirect_uri") String uri);
    @PostMapping("/refresh/token")
    public String updateAccessToken(@RequestHeader("Content-type") String Content,
                                 @RequestParam("grant_type") String grant,
                                 @RequestParam("client_id") String client,
                                 @RequestParam("refresh_token") String refresh_token,
                                 @RequestParam("client_secret") String secret);
}

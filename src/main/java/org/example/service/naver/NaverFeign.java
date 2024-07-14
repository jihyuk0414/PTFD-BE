package org.example.service.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverApi",url = "https://nid.naver.com/oauth2.0")
public interface NaverFeign {
    //authorize?client_id=3OGCudku4yOAQaRE_ou3&response_type=code&redirect_uri=http://localhost:8080/login/oauth2/code/naver&state=1234
    @PostMapping("/token")
    public String getToken(@RequestParam("client_id") String client_id,
                      @RequestParam("client_secret") String secret,
                      @RequestParam("grant_type") String grant,
                      @RequestParam("code") String code);

    @PostMapping("/token")
    public String delToken(@RequestParam("client_id") String client_id,
                           @RequestParam("client_secret") String secret,
                           @RequestParam("grant_type") String grant,
                           @RequestParam("access_token") String token);
}


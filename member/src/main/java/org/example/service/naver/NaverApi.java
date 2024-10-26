package org.example.service.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "NaverOpenApi" ,url = "https://openapi.naver.com")
public interface NaverApi {
    @GetMapping("/v1/nid/me")
    public String UserInfo(@RequestHeader("Authorization") String auth);
}

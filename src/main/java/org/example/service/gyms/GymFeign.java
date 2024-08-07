package org.example.service.gyms;

import org.example.dto.gym.GymsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "gym", url = "https://api.odcloud.kr/api/15071029/v1/uddi:7c6a4eaa-179a-469e-bb19-cd39e221190c")
public interface GymFeign {
    @GetMapping
    public GymsDto getGyms(@RequestParam("page") int page,
                           @RequestParam("perPage") int perPage,
                           @RequestParam("serviceKey") String serviceKey);
}

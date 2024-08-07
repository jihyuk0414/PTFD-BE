package org.example.service.gyms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.gym.GymDto;
import org.example.dto.gym.GymsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymService {
    private final GymFeign gymFeign;

    @Value("${spring.keys}")
    private String keys;
    //yml에서 받아옵니다.

    public GymsDto getGymsForMain()
    {
        log.info("sendgyms");
        return gymFeign.getGyms(1,5,keys);
    }

    public GymsDto getGymsAllWithFilter(String location)
    {
        GymsDto gymsDto = gymFeign.getGyms(1,100,keys);
        List<GymDto> filteredGyms = gymsDto.getData().stream()
                .filter(gym -> gym.getGymLocation().equals(location)) // 조건에 맞게 필터링
                .collect(Collectors.toList());

        gymsDto.setData(filteredGyms);

        return gymsDto;
    }
}

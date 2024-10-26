package org.example.dto.gym;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class GymsDto {
    //api로 받아오는 포멧입니다.
    private int currentCount;
    private List<GymDto> data;
    private int matchCount;
    private int page;
    private int perPage;
    private int totalCount;
}

package org.example.dto.gym;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GymDto {
    //주석 처리할 것은, api측에선 주지만 쓰지 않을 것 같은 필드들입니다.

//    @JsonProperty("반다비시설")
//    private String gyn;

    @JsonProperty("번호")
    private int gymId;

    @JsonProperty("소재지")
    private String gymLocationDetail;

    @JsonProperty("시_도")
    private String gymLocation;

    @JsonProperty("시설명")
    private String gymName;

//    @JsonProperty("운영기관")
//    private String operatingAgency;

//    @JsonProperty("장애인스포츠강좌 바우처시설")
//    private String voucherFacility;

    @JsonProperty("전화번호")
    private String phoneNumber;

    @JsonProperty("홈페이지")
    private String homepage;

}

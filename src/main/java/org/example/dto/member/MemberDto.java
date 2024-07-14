package org.example.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberDto {

    @Email
    @Schema(description = "이메일", example = "jj1234@naver.com")
    private String email;

    @Schema(description = "이름",example = "김민우")
    private String userName;

    @Schema(description = "비밀번호",example = "영문,숫자,특수기호 포함8자리이상")
    private String password;

    @Schema(description = "프로필 사진")
    private String profileImage;

    @Schema(description = "닉네임")
    private String nickName;
    private int point;
    private int follower;
    private int following;
    private int socialType;
    private String memberInfo;
    private String role;
    private char gender;

    @Builder
    public MemberDto(String role,String memberInfo,String email, String nickName, String profileImage, String userName, String password, int follower, int following, int point, int socialType,char gender){
        this.email=email;
        this.nickName=nickName;
        this.profileImage = profileImage;
        this.userName=userName;
        this.password=password;
        this.follower=follower;
        this.following=following;
        this.point=point;
        this.socialType = socialType;
        this.memberInfo= memberInfo;
        this.role=role;
        this.gender=gender;
    }

}

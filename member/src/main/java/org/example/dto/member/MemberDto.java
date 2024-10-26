package org.example.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberDto {
    @Email
    private String email;

    private String userName;

    private String password;

    private String profileImage;

    private String nickName;
    private int point;
    private int follower;
    private int following;
    private int socialType;
    private String memberInfo;
    private String role;

    @Builder
    public MemberDto(String role,String memberInfo,String email, String nickName, String profileImage, String userName, String password, int follower, int following, int point, int socialType){
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
    }

}

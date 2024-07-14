package org.example.dto.follow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class FollowDto {
    @Schema(description = "이메일", example = "jj1234@naver.com")
    private String email;
}

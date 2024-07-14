package org.example.dto.login;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.jwt.JwtDto;

@Data
@RequiredArgsConstructor
public class LoginSuccessDto {
    private String message;
    private JwtDto jwtDto;

    @Builder
    public LoginSuccessDto(String message,JwtDto jwtDto){
        this.jwtDto=jwtDto;
        this.message=message;
    }
}

package org.example.dto.signup;

import lombok.Builder;
import lombok.Data;

@Data
public class SignUpRes {
    private String state;
    private String message;

    @Builder
    public SignUpRes(String state, String message){
        this.state=state;
        this.message=message;
    }
}

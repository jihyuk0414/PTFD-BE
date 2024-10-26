package org.example.jwt;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokenError {
    String state;

    @Builder
    public TokenError(String state){

        this.state=state;
    }
}

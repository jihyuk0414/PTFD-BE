package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshDto {
    String refresh_token;
    String access_token;
    @Builder
    public RefreshDto(String access_token){
        this.access_token=access_token;
    }
}

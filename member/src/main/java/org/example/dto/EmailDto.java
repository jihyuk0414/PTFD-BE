package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailDto {
    private String email;

    @Builder
    public EmailDto(String email){
        this.email=email;
    }
}

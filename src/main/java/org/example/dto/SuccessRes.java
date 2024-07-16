package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SuccessRes {
    private String postName;
    private String message;
    @Builder
    public SuccessRes(String postName, String message){
        this.message=message;
        this.postName=postName;
    }
}

package org.example.dto.exception;

import lombok.Builder;
import lombok.Data;

@Data
public class ExceptionRes {
    private String state;
    private String message;

    @Builder
    public ExceptionRes(String state, String message){
        this.message=message;
        this.state=state;
    }

}

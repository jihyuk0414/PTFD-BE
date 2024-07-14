package org.example.dto.exception;

import lombok.Builder;
import lombok.Data;

@Data
public class ExceptionResponse {
    private String state;
    private String message;

    @Builder
    public ExceptionResponse(String state, String message){
        this.state=state;
        this.message=message;
    }
}

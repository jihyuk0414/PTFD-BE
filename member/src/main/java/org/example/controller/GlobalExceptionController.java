package org.example.controller;

import org.example.dto.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> NullPointerException(NullPointerException n) {
        ExceptionResponse response = new ExceptionResponse("잘못된 형식의 요청", n.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> IOException(IOException e){
        ExceptionResponse response = new ExceptionResponse("현재 이미지 저장 공간 부족", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> NoSuchElementException(NoSuchElementException e){
        ExceptionResponse response = new ExceptionResponse("요청 이메일이나 닉네임이 존재하지 않음", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}

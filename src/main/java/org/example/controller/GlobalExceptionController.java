package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.exception.ExceptionRes;
import org.example.dto.exception.CustomMailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionRes> NullPointerException(NullPointerException n) {
        log.error(n.getMessage());
        ExceptionRes response = new ExceptionRes("잘못된 형식의 요청", n.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionRes> IOException(IOException e){
        logger.error(e.getMessage());
        ExceptionRes response = new ExceptionRes("현재 이미지 저장 공간 부족", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionRes> NoSuchElementException(NoSuchElementException e){

        ExceptionRes response = new ExceptionRes("요청 상품이 존재하지 않음", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomMailException.class)
    public ResponseEntity<ExceptionRes> MailException(CustomMailException m){
        ExceptionRes response = new ExceptionRes("메일 전송 과정에서 문제가 발생했습니다", m.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

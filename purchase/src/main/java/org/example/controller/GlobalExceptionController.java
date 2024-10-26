package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.rmi.AlreadyBoundException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionRes> SQLException(SQLException s)
    {
        ExceptionRes response = new ExceptionRes("PURCHASE DB 응답이 없습니다.", s.getMessage()) ;
        //REPOSITORY를 그대로 활용하기 때문에, QUERY 관련 EXCEPTION이 발생하지 않습니다.
        //해당 오류는 ORDER, PURCHASE 관련하여 발생하는 오류만 잡습니다.
        //SQLEXCEPTION의 발생 경우는 , 연결 관련 문제 혹은 무결성 문제만 존재합니다.
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionRes> NullPointerException(NullPointerException n) {
        ExceptionRes response = new ExceptionRes("잘못된 형식의 요청입니다.", n.getMessage());
        //요청과정 NULL (변수명 등 안맞을 경우)
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionRes> NoSuchElementException(NoSuchElementException e){
        ExceptionRes response = new ExceptionRes("요청 상품이 존재하지 않음", e.getMessage());
        log.info("noelementexceptionokay");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberContainerException.class)
    public ResponseEntity<ExceptionRes> ServerClosedException(MemberContainerException se){
        ExceptionRes response = new ExceptionRes("Member-Container에서 문제 발생", se.getMessage()) ;
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ExceptionRes> ConnectException(ConnectException se){
        ExceptionRes response = new ExceptionRes("Member-Container가 동작하지 않습니다", se.getMessage()) ;
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE) ;
    }

    @ExceptionHandler(AlreadySoldOutException.class)
    public ResponseEntity<ExceptionRes> AlreadySoldOutException(AlreadySoldOutException se){
        ExceptionRes response = new ExceptionRes("이미 판매 완료된 상품입니다. ", se.getMessage()) ;
        return new ResponseEntity<>(response, HttpStatus.CONFLICT) ;
    }

    //보안 관련
    @ExceptionHandler(PaymentClaimAmountMismatchException.class)
    public ResponseEntity<ExceptionRes> PaymentClaimAmountMismatchException (PaymentClaimAmountMismatchException o){
        ExceptionRes response = new ExceptionRes("사용자 실제 결제 금액과 결제 주장 금액이 다릅니다.", o.getMessage()) ;
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN) ;
    }
}

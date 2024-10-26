package org.example.dto.exception;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomMailException extends RuntimeException {
    String msg = ("메일 전송 과정에서 문제가 발생하였습니다.");


}
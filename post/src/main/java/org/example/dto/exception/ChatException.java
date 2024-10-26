package org.example.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatException extends RuntimeException{
    String msg = "chat과의 통신 도중 문제 발생";
}

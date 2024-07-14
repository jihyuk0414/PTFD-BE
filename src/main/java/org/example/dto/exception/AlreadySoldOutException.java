package org.example.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlreadySoldOutException extends Exception{

    String message = "이미 판매 된 상품입니다. 결제한 금액을 취소합니다.";

}

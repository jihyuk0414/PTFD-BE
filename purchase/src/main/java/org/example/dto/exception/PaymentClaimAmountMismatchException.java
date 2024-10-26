package org.example.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentClaimAmountMismatchException extends RuntimeException{
    String message = "사용자 결제 총 금액과 결제 주장 금액이 다릅니다." ;
}

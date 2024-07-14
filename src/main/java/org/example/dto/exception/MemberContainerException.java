package org.example.dto.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberContainerException extends RuntimeException {
    String message = "MEMBER CONTAINER에서 문제가 발생하였습니다.";

}

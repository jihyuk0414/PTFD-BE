package org.example.dto.portone;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelResponse {

    String status;
    String id ;
    String totalAmount ;
    String taxfreeAmount ;
    String vatAmount ;
    String reason;
    String requestedAt;
}

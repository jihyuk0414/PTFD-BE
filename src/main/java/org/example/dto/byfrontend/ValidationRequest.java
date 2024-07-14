package org.example.dto.byfrontend;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dto.forbackend.PaymentsReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ValidationRequest {
    String payment_id;
    int total_point; //차이 금액

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate created_at;

    private List<PaymentsReq> payments_list;
}

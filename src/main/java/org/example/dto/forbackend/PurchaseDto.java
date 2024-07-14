package org.example.dto.forbackend;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PurchaseDto {
    private String email;
    private int total_point;
    private List<PaymentsReq> payments_list;
}

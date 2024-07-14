package org.example.dto.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data

public class PaymentsRes {
    @Schema(description = "포인트 충전 여부")
    private Boolean charge;
    @Schema(description = "필요 충전 포인트")
    private int point;
    private String message;
    @Builder
    public PaymentsRes(Boolean charge, int point, String message){
        this.charge=charge;
        this.point=point;
        this.message=message;
    }
}

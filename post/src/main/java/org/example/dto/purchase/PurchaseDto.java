package org.example.dto.purchase;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;



@Data
@RequiredArgsConstructor
public class PurchaseDto {
    private boolean success;
    private int soldOutIds;

    @Builder
    public PurchaseDto(boolean success, int soldOutIds){
        this.soldOutIds=soldOutIds;
        this.success=success;
    }
}

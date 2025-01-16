package org.example.dto.forbackend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSaveRequest {
    private String seller ;
    private String consumer ;
    private Long post_id;
    private int post_point;
    private LocalDate purchase_at ;

}

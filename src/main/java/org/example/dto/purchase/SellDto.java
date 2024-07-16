package org.example.dto.purchase;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SellDto {
    private String email;
    private List<Long> post_id;
}

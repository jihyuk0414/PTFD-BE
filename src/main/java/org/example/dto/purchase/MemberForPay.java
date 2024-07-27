package org.example.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberForPay {
    private int point;
    private int social_type;
}

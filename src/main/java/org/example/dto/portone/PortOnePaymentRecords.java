package org.example.dto.portone;

import lombok.*;

@Data
@RequiredArgsConstructor
public class PortOnePaymentRecords {

    String status;

    String requestedAt;

    PaymentAmount amount;

    String orderName;
}


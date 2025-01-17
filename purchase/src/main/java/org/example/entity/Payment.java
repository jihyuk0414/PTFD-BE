package org.example.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table(name = "payment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column("payment_pid")
    private int paymentPid;

    @Column("payment_id")
    private String paymentId; //결제 id

    @Column("status")
    private String status;

    @Column("payment_at")
    private Timestamp paymentAt;
    //결제 시간

    @Column("total_amount")
    private int totalAmount;

    @Column("point_name")
    private String pointName;
    //구매한 point명

    @Column("user_email")
    private String memberEmail;

    @Builder
    public Payment(String paymentId, String status, Timestamp paymentAt, String pointName, int totalAmount, String memberEmail) {
        this.paymentId = paymentId;
        this.status = status;
        this.paymentAt = paymentAt;
        this.pointName = pointName;
        this.totalAmount = totalAmount;
        this.memberEmail = memberEmail;
    }
}

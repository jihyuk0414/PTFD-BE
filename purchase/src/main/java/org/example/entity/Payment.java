package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "payment")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_pid")
    private int paymentPid;

    @Column(name = "payment_id")
    private String paymentId ; //결제 id

    @Column(name = "status")
    private String status;

    @Column(name = "payment_at")
    private Timestamp paymentAt;
    //결제 시간

    @Column(name = "total_amount")
    private int totalAmount ;

    @Column(name = "point_name")
    private String pointName;
    //구매한 point명

    @Column(name = "user_email")
    private String memberEmail;



    @Builder
    public Payment(String paymentid, String status, Timestamp purchaseat, String ordername, int totalamount, String useremail) {
        this.paymentId = paymentid;
        this.status = status;
        this.paymentAt = purchaseat;
        this.pointName = ordername;
        this.totalAmount = totalamount;
        this.memberEmail = useremail;
    }

}

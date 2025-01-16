package org.example.entity;

//import jakarta.persistence.*;

//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.example.dto.forbackend.OrderSaveRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;



@Table(name = "orders")
//@Entity
@Getter
@RequiredArgsConstructor
public class Order {

    @Id
    @Column("order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column("post_id")
    private Long postId ;

    @Column("order_price")
    private int orderPrice ;

    @Column("consumer_email")
    private String consumerEmail ;

    @Column("seller_email")
    private String sellerEmail ;

    @Column("order_at")
    private LocalDate orderAt;

    @Builder
    public Order (Long postId, int orderPrice, String consumerEmail, String sellerEmail, LocalDate orderAt)
    {
        this.postId = postId ;
        this.orderPrice=orderPrice ;
        this.consumerEmail = consumerEmail;
        this.sellerEmail = sellerEmail;
        this.orderAt= orderAt ;
    }

    public static Order ToOrder(OrderSaveRequest orderSaveRequest)
    {
        return Order.builder()
                .postId(orderSaveRequest.getPost_id())
                .orderPrice(orderSaveRequest.getPost_point())
                .consumerEmail(orderSaveRequest.getConsumer())
                .sellerEmail(orderSaveRequest.getSeller())
                .orderAt(orderSaveRequest.getPurchase_at())
                .build() ;
    }

}




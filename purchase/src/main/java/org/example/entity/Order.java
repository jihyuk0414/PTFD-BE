package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.dto.forbackend.OrderSaveRequest;

import java.time.LocalDate;



@Table(name = "orders")
@Entity
@Getter
@RequiredArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "post_id")
    private Long postId ;

    @Column(name = "order_price")
    private int orderPrice ;

    @Column(name = "consumer_email")
    private String consumerEmail ;

    @Column(name = "seller_email")
    private String sellerEmail ;

    @Column(name = "order_at")
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




package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.forbackend.OrderSaveRequest;
import org.example.dto.forbackend.PaymentsRes;
import org.example.entity.Order;
import org.example.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    //금액부족시 purchas에서 바로 orderservice 저장


    //금액이 있어서 저장만 해주면 될때
    public ResponseEntity<PaymentsRes> saveOrderRequstByMember(List<OrderSaveRequest> orderSaveRequestlist)
    {

        for (OrderSaveRequest orderSaveRequest : orderSaveRequestlist) {

            Order order = Order.builder()
                    .orderPrice(orderSaveRequest.getPost_point())
                    .orderAt(orderSaveRequest.getPurchase_at())
                    .consumerEmail(orderSaveRequest.getConsumer())
                    .sellerEmail(orderSaveRequest.getSeller())
                    .postId(orderSaveRequest.getPost_id())
                    .build();

            orderRepository.save(order);

        }

        PaymentsRes paymentsRes = PaymentsRes.builder()
                .point(0)
                .message("구매 및 결제 완료 되었습니다")
                .charge(false)
                .build();

        return ResponseEntity.ok().body(paymentsRes) ;
    }
}

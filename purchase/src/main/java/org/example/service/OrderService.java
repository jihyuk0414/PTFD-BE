package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.forbackend.OrderSaveRequest;
import org.example.dto.forbackend.PaymentsRes;
import org.example.entity.Order;
import org.example.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public ResponseEntity<PaymentsRes> saveOrderRequstByMember(List<OrderSaveRequest> orderSaveRequestlist)
    {
        List<Order> orders = orderSaveRequestlist.stream()
                .map(orderSaveRequest -> Order.builder()
                        .orderPrice(orderSaveRequest.getPost_point())
                        .orderAt(orderSaveRequest.getPurchase_at())
                        .consumerEmail(orderSaveRequest.getConsumer())
                        .sellerEmail(orderSaveRequest.getSeller())
                        .postId(orderSaveRequest.getPost_id())
                        .build())
                .collect(Collectors.toList());

        // 생성된 Order 객체들을 저장
        // 다만 현재는 netty, 그러므로 .block()등의 메소드로 producer가 기다릴 수 없음
        //현재 기다릴 수 있는 operator 측에서 작업하는 것으로 수정
//        Flux.fromIterable(orders)
//                .flatMap(orderRepository::save)
//                .subscribe();

        Flux.fromIterable(orders)
                .flatMap(orderRepository::save)
                .subscribe();

        PaymentsRes paymentsRes = PaymentsRes.builder()
                .point(0)
                .message("구매 및 결제 완료 되었습니다")
                .charge(false)
                .build();

        return ResponseEntity.ok().body(paymentsRes) ;
    }
}

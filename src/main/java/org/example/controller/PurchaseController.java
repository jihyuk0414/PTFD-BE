package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.byfrontend.ValidationRequest;
import org.example.dto.forbackend.OrderSaveRequest;
import org.example.dto.forbackend.PaymentsRes;
import org.example.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final PaymentService paymentService;
    private final OrderService orderService ;
    //paymentservice는 portone의 결제 확인 정보를 포함한 내용을
    //purchase는 결제 확인 정보와 검증을 포함한 내용을 전달해야 합니다.


    //한개 결제시 .

    @PostMapping("/payments/complete/{useremail}")
    public Mono<PaymentsRes> validatepaymentone(@PathVariable(value = "useremail") String useremail,
                                                @RequestBody ValidationRequest validation)
    {
        return paymentService.getPortOneToken()
                .flatMap(PortOnetoken -> paymentService.getPaymentRecordsByPortOne(validation.getPayment_id(), PortOnetoken)
                        .flatMap(purchasecheckresponsewebclient ->
                                paymentService.validateandSave(purchasecheckresponsewebclient,validation.getPayment_id(), validation.getTotal_point(),useremail,PortOnetoken)
                                .flatMap(validateresult -> {
                                    return paymentService.sendPaymentSuccessRequestToMember(purchasecheckresponsewebclient.getOrderName(),purchasecheckresponsewebclient.getRequestedAt(),validation,useremail,PortOnetoken) ;
                                })));

    }


    @PostMapping("/payments/complete")
    public ResponseEntity<PaymentsRes> saveOrder(@RequestBody List<OrderSaveRequest> orderSaveRequestList)
    {
        return orderService.saveOrderRequstByMember(orderSaveRequestList) ;
    }
}

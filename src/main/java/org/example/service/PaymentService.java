package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.byfrontend.ValidationRequest;
import org.example.dto.exception.AlreadySoldOutException;
import org.example.dto.exception.MemberContainerException;
import org.example.dto.exception.PaymentClaimAmountMismatchException;
import org.example.dto.forbackend.PaymentsRes;
import org.example.dto.forbackend.PurchaseDto;
import org.example.dto.portone.*;
import org.example.entity.Payment;
import org.example.repository.PaymentRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final WebClient portOneWebClient = WebClient.builder().baseUrl("https://api.portone.io").build();
    //이 portonewebclient는 docker로 올릴떄도 변경하지 않아도 됩니다.

//    private final WebClient webClientforMember = WebClient.builder().baseUrl("http://localhost:8080/member").build() ;
    private final WebClient webClientforMember = WebClient.builder().baseUrl("http://member-service:81/member").build() ;

    //이 webclientformember는 docker container화 과정에서 변경해주어야 합니다.

    private final PaymentRepository paymentRepository;


    //portone에서 결제 조회를 하려면, portonetoken이 필요합니다. 해당 token을 가져오는부 입니다.
    //사이트 내부적으로 너무 빨리 갱신하여, 매번 가져오는 형태로 변경하였습니다.
    public Mono<String> getPortOneToken() {
        PortoneTokenRequest portoneTokenRequest = new PortoneTokenRequest("hM546ISQZ7vQ61xw0eTV0hk7GpRDS48Pr92uTBGbCc5z9u4iSC3DiMed3SHBohBQHWj8ZEPHJF6J8VNA");

        Mono<String> PortOneToken;

        PortOneToken = portOneWebClient
                .post()
                .uri("/login/api-secret")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(portoneTokenRequest))
                .retrieve()
                .bodyToMono(PortoneTokenResponse.class)
                .map(PortoneTokenResponse::getAccessToken);
        return PortOneToken;

    }

    public Mono<PortOnePaymentRecords> getPaymentRecordsByPortOne(String paymentId, String portOneToken)
    {
        return portOneWebClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .header("Authorization", "Bearer " + portOneToken)
                .retrieve()
                .bodyToMono(PortOnePaymentRecords.class);

    }




    //결제정보의 Dateformat을 변경하고, 저장합니다. (RFC 3339 date-time)의 형식을 따릅니다.
    //이를 Timestamp 형태로 변경합니다. (string으로 받아오기 떄문입니다)
    public Timestamp changeDateFormat(String requestAt)
    {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(requestAt);
        Timestamp requestedAtTimestamp = Timestamp.from(offsetDateTime.toInstant());

        return requestedAtTimestamp;

    }

    //PortOne에 저장된 정보와 , 결제 정보가 일치한지 확인합니다.
    //일치시, 해당 정보를 저장하고 불일치시 결제 취소 요청을 보냅니다.
    public Mono<Boolean> validateandSave (
            PortOnePaymentRecords portOnePaymentRecords,
            String paymentId,
            int frontPaymentClaim,
            String useremail,
            String portOneToken)
    {
        //프론트에서 전달한 결제 정보가 일치한지, portone에서 실제로 했던 결제 금액이 일치한지 확인
        if (portOnePaymentRecords.getAmount().getTotal() == frontPaymentClaim) {
            log.info("결제 정보가 일치합니다. 결제 정보를 저장합니다.");
            //portone에서 제공하는 날짜 type에 따른 교환
            Timestamp purchaseAt = changeDateFormat(portOnePaymentRecords.getRequestedAt()) ;

            Payment payment = Payment.builder()
                    .paymentid(paymentId)
                    .status(portOnePaymentRecords.getStatus())
                    .purchaseat(purchaseAt) // 변환 필요
                    .ordername(portOnePaymentRecords.getOrderName())
                    .totalamount(frontPaymentClaim)
                    .build();

            paymentRepository.save(payment) ; // 검증 정보가 문제 없을시, 결제 완료된걸 저장.

            return Mono.just(Boolean.FALSE); //문제없음

        } else {
            OffsetDateTime currentDateTime = OffsetDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");//결제 취소시간

            cancelPayment(paymentId,currentDateTime.format(formatter), portOnePaymentRecords.getOrderName(),
                    frontPaymentClaim, "결제 금액과 DB 확인 결과 맞지 않습니다", "DENIED",portOneToken) ;
            //결제 취소 완료. 이후 exception 유발 필요-
            throw new PaymentClaimAmountMismatchException();

        }
    }

    public Mono<PaymentsRes> sendPaymentSuccessRequestToMember(String orderName, String requestedAt, ValidationRequest validationRequest, String email, String portOneToken)
    {

        //오류가 없을때
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setEmail(email);
        purchaseDto.setTotal_point(validationRequest.getTotal_point());
        purchaseDto.setPayments_list(validationRequest.getPayments_list());

        return webClientforMember.post()
                .uri("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(purchaseDto))
                .retrieve()
                .bodyToMono(PaymentsRes.class)
                .onErrorResume(WebClientRequestException.class, ex -> {
                    OffsetDateTime currentDateTime = OffsetDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    return cancelPayment(validationRequest.getPayment_id(), currentDateTime.format(formatter), orderName, validationRequest.getTotal_point(), "Failed to send payment request", "CANCELLED", portOneToken)
                            .then(Mono.error(new MemberContainerException()));
                    //MSA구조에선, 상대 서버가 안켜져도 결제엔 문제가 없어야 된다고 생각했습니다.
                    //Member server가 켜지지 않아도. 결제 했던건 취소됩니다.
                    //++ member 서버에서 에외처리를 해주셔도 통신 과정에선 EXCEPTION으로 처리됩니다.
                    //이 모든 경우를 잡았습니다.
                })
                .flatMap(paymentsRes -> {
                    if ("예약하시려는 수업중 마감된 수업이있습니다".equals(paymentsRes.getMessage()) || "수업이 없습니다".equals(paymentsRes.getMessage())) {
                        OffsetDateTime currentDateTime = OffsetDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

                        return cancelPayment(validationRequest.getPayment_id(), currentDateTime.format(formatter), orderName, validationRequest.getTotal_point(),
                                "이미 마감된 수업입니다.", "CANCELLED", portOneToken)
                                .then(Mono.error(new AlreadySoldOutException()));
                    } else {
                        return Mono.just(paymentsRes);
                    }
                });


    }

    //결제 취소 PORTONE에게 요청
    public Mono<Void> cancelPayment (String paymentId, String requestedAt, String orderName, int totalAmount, String cancelReason, String paymentStatus, String portOneToken )
    {
        WebClient cancelwebClient = WebClient.builder().baseUrl("https://api.portone.io").build();
        CancelRequest cancelRequest = new CancelRequest(cancelReason) ;

        cancelwebClient
                .post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + portOneToken)
                .body(BodyInserters.fromValue(cancelRequest))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Failed to cancel payment with paymentId: {}", paymentId);
                    return Mono.empty();
                    //결제 취소가 반려되면 해당 정보는 사용자에게 띄우는 것이 아니라 PORTONE측과의 문제입니다.
                    //그렇기 때문에 로그에 남겨 해당 정보를 인지하고, 해당 PAYMENTID를 직접 취소 요청혹은 PORTONE과 합의해야 된다고 생각했습니다.
                    //그래서 EXCEPTION처리를 포기하고, ERROR LEVEL LOG로 남겼습니다.
                })
                .bodyToMono(CancelResponse.class)
                .subscribe(
                        cancelResponse -> {
                        Timestamp purchaseAt = changeDateFormat(requestedAt) ;

                        Payment cancelpayment = Payment.builder()
                                .paymentid(paymentId)
                                .status(paymentStatus)
                                .purchaseat(purchaseAt)
                                .ordername(orderName)
                                .totalamount(totalAmount)
                                .build();

                        paymentRepository.save(cancelpayment);

                        });
        return Mono.empty();

    }



}

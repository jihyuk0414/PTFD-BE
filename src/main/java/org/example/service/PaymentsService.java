package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.send.Content;
import org.example.dto.send.Link;
import org.example.dto.send.TemplateObject;
import org.example.dto.post.PostFeignReq;
import org.example.dto.post.PostFeignRes;
import org.example.dto.post.MessageRes;
import org.example.dto.purchase.*;
import org.example.entity.Member;
import org.example.repository.member.MemberRepository;
import org.example.service.kakao.KakaoService;
import org.example.service.purchase.PostFeign;
import org.example.service.purchase.PurchaseFeign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsService {

    private final MemberRepository memberRepository;
    private final PostFeign postFeign;
    private final PurchaseFeign purchaseFeign;
    private final KakaoService kakaoService;

    @Transactional
    public PaymentsRes purchase(PurchaseDto purchaseDto, String email) throws JsonProcessingException {
        HashMap<String,Integer> sellers = new HashMap<>();
        List<Long> sellPostId = new ArrayList<>();
        Optional<Member> consumer = memberRepository.findByEmail(email);
        consumer.orElseThrow();

        int consumerPoint=consumer.get().getPoint() - purchaseDto.getTotal_point();
        log.info(String.valueOf(consumerPoint));

        if (purchaseDto.getTotal_point()>consumer.get().getPoint()){return PaymentsRes.builder().charge(true).point(Math.abs(consumerPoint)).message("포인트 충전 필요").build();}
        else {
            for (PaymentsReq req : purchaseDto.getPayments_list()){
                req.setConsumer(purchaseDto.getEmail());
                if(purchaseOne(req, sellers, sellPostId)){return PaymentsRes.builder().charge(false).message("수업이 없습니다").build();}
            }
            log.info(sellPostId.toString());
            log.info(String.valueOf(sellers.size()));
            PostFeignRes postFeignRes = postFeign.SoldOut(PostFeignReq.builder()
                    .post_id(sellPostId)
                    .email(email)
                    .build());

            if (postFeignRes.isSuccess()){
                memberRepository.updatePoint(consumerPoint,email);
                for (String sellerEmail : sellers.keySet()){
                    log.info(sellerEmail);
                    log.info(String.valueOf(sellers.get(sellerEmail)));
                    memberRepository.updatePoint(sellers.get(sellerEmail),sellerEmail);
                }
                purchaseFeign.saveOrder(purchaseDto.getPayments_list());
                if (consumer.get().getSocialType() == 1) //카카오
                {
                    for (PaymentsReq paymentsReq: purchaseDto.getPayments_list()){
                        log.info("카카오 회원");
                        sendMessage(paymentsReq.getPost_id());
                    }
                    postFeign.SendEmailToSeller(purchaseDto.getPayments_list());
                }
                else if (consumer.get().getSocialType() == 0 ) //일반 회원가입 유저라면
                {
                    log.info("일반 회원");
                    postFeign.SendEmail(purchaseDto.getPayments_list(),email);//이메일 전송
                }
                return PaymentsRes.builder().charge(false).message("예약 성공").build();
            }
            else {
                log.info(String.valueOf(postFeignRes.getSoldOutIds()));
                memberRepository.updatePoint(consumer.get().getPoint(),email);
                return PaymentsRes.builder()
                        .charge(null)
                        .message("예약하시려는 수업중 마감된 수업이있습니다")
                        .build();
            }
        }

    }

    @Transactional
    public PaymentsRes purchaseSuccess(PurchaseDto purchaseDto) throws JsonProcessingException {
        HashMap<String,Integer> sellers = new HashMap<>();
        List<Long> sellPostId = new ArrayList<>();
        Optional<Member> consumer = memberRepository.findByEmail(purchaseDto.getEmail());
        consumer.orElseThrow();
        memberRepository.updatePoint(0,purchaseDto.getEmail());
        for (PaymentsReq req : purchaseDto.getPayments_list()){
            req.setConsumer(purchaseDto.getEmail());
            if(purchaseOne(req, sellers, sellPostId)){return PaymentsRes.builder().charge(false).message("수업이 없습니다").build();}
        }
        PostFeignRes postFeignRes = postFeign.SoldOut(PostFeignReq.builder()
                .post_id(sellPostId)
                .email(purchaseDto.getEmail()).
                build());
        if (postFeignRes.isSuccess()){
            for (String email : sellers.keySet()){
                memberRepository.updatePoint(sellers.get(email),email);
            }

            purchaseFeign.saveOrder(purchaseDto.getPayments_list());

            if (consumer.get().getSocialType() == 1) //카카오 라면
            {
                for (PaymentsReq paymentsReq: purchaseDto.getPayments_list()){
                    sendMessage(paymentsReq.getPost_id());
                }
                postFeign.SendEmailToSeller(purchaseDto.getPayments_list());
            }
            else if (consumer.get().getSocialType() == 0 ) //일반 회원가입 유저라면
            {
                postFeign.SendEmail(purchaseDto.getPayments_list(),purchaseDto.getEmail()); //이메일 전송
            }
            return PaymentsRes.builder().charge(false).message("예약 성공").build();
        }
        else {
            log.info(String.valueOf(postFeignRes.getSoldOutIds()));
            memberRepository.updatePoint(consumer.get().getPoint(),consumer.get().getEmail());
            return PaymentsRes.builder()
                    .charge(null)
                    .message("예약하시려는 수업중 마감된 수업이있습니다")
                    .build();
        }
    }

    @Transactional
    public boolean purchaseOne(PaymentsReq req,HashMap<String,Integer> sellers,List<Long> sellPostId){
        Optional<Member> seller = memberRepository.findByEmail(req.getSeller());
        if (seller.isEmpty()){return true;}
        if (sellers.containsKey(seller.get().getEmail())){
            int total = sellers.get(seller.get().getEmail())+ req.getPost_point();
            log.info(String.valueOf(total));
            sellers.put(seller.get().getEmail(),total);
        }
        else {sellers.put(seller.get().getEmail(),seller.get().getPoint()+ req.getPost_point());}
        sellPostId.add(req.getPost_id());
        return false;
    }

    public void sendMessage(Long postId) throws JsonProcessingException {
        log.info("메시지 로직 동작");
        log.info(postId.toString());
        MessageRes Post= postFeign.getImage(postId);
        log.info(Post.getPost_name());
        Content content = Content.builder()
                .title("test")
                .image_url(Post.getImage_real())
                .link(Link.builder().web_url("http://localhost:3000").build())
                .description("예약되었습니다.")
                .build();
        TemplateObject templateObject = TemplateObject.builder()
                .content(content)
                .build();
        kakaoService.sendRealImage(templateObject);
    }
}

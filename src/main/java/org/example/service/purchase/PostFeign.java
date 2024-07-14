package org.example.service.purchase;

import org.example.dto.post.PostFeignReq;
import org.example.dto.post.PostFeignRes;
import org.example.dto.post.MessageRes;
import org.example.dto.purchase.PaymentsReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//@FeignClient(name = "postApi",url = "http://localhost:7080/post")
@FeignClient(name = "postApi",url = "http://post-service:70/post")
public interface PostFeign {
    @PostMapping("/payments/sell")
    public PostFeignRes SoldOut(@RequestBody PostFeignReq postFeignReq);

    @PostMapping(value = "/image",consumes = MediaType.APPLICATION_JSON_VALUE)
    public MessageRes getImage(@RequestParam("post_id") Long postId);

    @PostMapping("/emails/{consumer_email}")
    public String SendEmail(@RequestBody List<PaymentsReq> paymentsReqList, @PathVariable("consumer_email") String consumer_email);

    @PostMapping("/emails")
    public String SendEmailToSeller(@RequestBody List<PaymentsReq> paymentsReqList);
}

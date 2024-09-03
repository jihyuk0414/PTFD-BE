package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;
import lombok.RequiredArgsConstructor;
import org.example.dto.gym.GymsDto;
import org.example.dto.post.*;
import org.example.dto.purchase.PaymentsReq;
import org.example.dto.purchase.PurchaseDto;
import org.example.dto.purchase.SellDto;
import org.example.dto.search.SearchDto;
import org.example.service.MailService;
import org.example.service.SearchService;
import org.example.service.WishListService;
import org.example.service.PostService;
import org.example.service.gyms.GymService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final WishListService wishListService;
    private final SearchService searchService;
    private final MailService mailService;
    private final GymService gymService;


    @PostMapping("/{email}")
    public ResponseEntity<SuccessRes> savePost(@PathVariable("email") String email,
                                                  @RequestPart("req") PostDto PostDto,
                                                  @RequestPart("img") MultipartFile img_post
                                                 ) throws IOException {
        log.info("상품 등록");
        return ResponseEntity.ok(postService.addPost(PostDto,email,img_post));
    }

    @DeleteMapping("/{post_id}/{email}")
    public ResponseEntity<SuccessRes> deletePost(@PathVariable("email") String email, @PathVariable("post_id") Long postId) throws IOException {
        return ResponseEntity.ok(postService.deletePost(postId,email));
    }

    // 게시글 수정 , email 필요, email 활용 검증 필요
    @PutMapping("/{post_id}/{email}")
    public ResponseEntity<SuccessRes> changePost(@PathVariable("email") String email,
                                                 @PathVariable("post_id") Long postId,
                                                 @RequestPart("img") MultipartFile img_post,
                                                 @RequestPart("req")  PostDto postDto
    ) throws IOException {
        return ResponseEntity.ok(postService.updatePost(img_post,postId,postDto,email));
    }
    // 페이징 형태로 변경
    @GetMapping("/page")
    public ResponseEntity<Page<PostDto>> getPostPage(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                                     @RequestParam(value = "nick_name",required = false, defaultValue = "null") String nick_name,
                                                    @RequestParam(name = "category_id", required = false) List<Integer> category_id,
                                                    @RequestParam(name = "location", required = false) List<String> location) {
        return ResponseEntity.ok(postService.findPostPage(page,nick_name,category_id,location));
    }

    @GetMapping("/mypage")
    public ResponseEntity<Page<PostDto>> getMyPostPage(@RequestParam(value = "page",required = false, defaultValue = "0") int page,@RequestParam("nick_name") String nickName) {
        return ResponseEntity.ok(postService.findMyPostPage(nickName,page));
    }
    //게시글 1개 검색
    @GetMapping("/detail/{post_id}/{email}")
    public ResponseEntity<PostDetailRes> getPost(@PathVariable("post_id") Long postId, @PathVariable("email") String email) {
        return ResponseEntity.ok(postService.findPostDetail(postId,email));
    }

    @PostMapping("/like/{post_id}/{email}")
    public ResponseEntity<SuccessRes> uploadLike(@PathVariable("post_id") Long postId, @PathVariable("email") String email){
        return ResponseEntity.ok(wishListService.likeRegistration(email, postId));
    }

    @GetMapping("/profile/like/{nick_name}")
    public ResponseEntity<Page<PostDto>> getLikePost(@RequestParam(value = "page",required = false, defaultValue = "0") int page,@PathVariable("nick_name") String nickName){
        return ResponseEntity.ok(wishListService.showLikePost(nickName,page));
    }

    @DeleteMapping("/like/{post_id}/{email}")
    public ResponseEntity<SuccessRes> deleteLikePost( @PathVariable("post_id") Long postId, @PathVariable("email") String email){
        return ResponseEntity.ok(wishListService.delLikePost(email,postId));
    }

    @PostMapping("/payments/sell")
    public PurchaseDto changeState(@RequestBody SellDto sellDto){
        int soldOut = wishListService.sellWishList(sellDto.getPost_id(),sellDto.getEmail());
        log.info("상태 변경 로직");
        log.info(String.valueOf(soldOut));
        if (soldOut==0){
            wishListService.successPay(sellDto.getPost_id());
            postService.changeState(sellDto.getPost_id());
            return PurchaseDto.builder().success(true).soldOutIds(soldOut).build();
        }
        else {return PurchaseDto.builder().success(false).soldOutIds(soldOut).build();}
    }

    @PostMapping("/search/word")
    public ResponseEntity<List<String>> searchWord(@RequestBody SearchDto searchDto){
        return ResponseEntity.ok(searchService.autoComplete(searchDto.getWord()));
    }


    @PostMapping("/search")
    public ResponseEntity<Page<PostDto>> searchFullWord
            (@RequestBody SearchDto searchDto,
             @RequestParam(name = "page",required = false,defaultValue = "0") int page,
             @RequestParam(name = "category_id", required = false) List<Integer> category_id,
             @RequestParam(name = "location", required = false) List<String> location,
             @RequestParam(name = "nick_name", required = false) String nickName){
        return ResponseEntity.ok(searchService.searchPost(searchDto.getPost_name(), page,category_id, location,nickName));
    }

    @PostMapping("/image")
    public PostForMessage getImage(@RequestParam("post_id") Long PostId){
        return postService.sendReservation(PostId);
    }

    @PostMapping("/emails/{consumer_email}")
    public ResponseEntity<String> SendEmail(@RequestBody List<PaymentsReq> paymentsReqList, @PathVariable("consumer_email") String consumer_email)
    {
        return ResponseEntity.ok(mailService.sendEmail(paymentsReqList,consumer_email));
    }
    @PostMapping("/emails")
    public ResponseEntity<String> SendEmailToSell(@RequestBody List<PaymentsReq> paymentsReqList)
    {
        return ResponseEntity.ok(mailService.sendEmailToSeller(paymentsReqList));
    }



    //여기서 부터 변경 부
    @GetMapping("/gyms/main")
    public ResponseEntity<GymsDto> getGyms()
    {
        return ResponseEntity.ok(gymService.getGymsForMain());
    }


    @GetMapping("/gyms/all")
    public ResponseEntity<GymsDto> getGymsAll(
            @RequestParam(name = "location",required = false) String location
    )
    {
        return ResponseEntity.ok(gymService.getGymsAllWithFilter(location));
    }

    @GetMapping("/chat")
    public PostForChat getPostForChat(@RequestParam("post_id") String postId){
        return postService.getPostForChatting(postId);
    }

    //추가
    @PutMapping("/update/nick_name")
    public ResponseEntity<String> ChangeNicknameByEmail(@RequestParam("nick_name") String nickName, @RequestParam("email") String email)
    {
        return ResponseEntity.ok(postService.changeNicknameByEmail(nickName, email));
    }

    @PutMapping("/update/user_profile")
    public ResponseEntity<String> changeProfileImgByEmail(@RequestParam("nick_name") String profileImg, @RequestParam("email") String email)
    {
        return ResponseEntity.ok(postService.changeprofileImgByEmail(profileImg,email));
    }

}

package org.example.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.post.PostDetailRes;
import org.example.dto.post.PostDto;
import org.example.dto.post.PostForMessage;
import org.example.dto.post.PostWishListCountDto;
import org.example.dto.purchase.PaymentsReq;
import org.example.dto.purchase.PurchaseDto;
import org.example.dto.purchase.SellDto;
import org.example.dto.search.SearchDto;
import org.example.dto.wish_list.WishListDto;
import org.example.service.MailService;
import org.example.service.SearchService;
import org.example.service.WishListService;
import org.example.service.PostService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name ="상품 게시판" , description = "상품 관련 API")
public class PostController {
    private final PostService postService;
    private final WishListService wishListService;
    private final SearchService searchService;
    private final MailService mailService;
    // 게시글 작성 - email 필요
    @Operation(summary = "상품 게시글 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "상품 게시글 작성 중 문제 발생")
    })

    @PostMapping("/{email}")
    public ResponseEntity<SuccessRes> savePost(@PathVariable("email") String email,
                                                  @RequestPart("req") PostDto PostDto,
                                                  @RequestPart("img") MultipartFile img_post
                                                 ) throws IOException {
        log.info("상품 등록");
        return ResponseEntity.ok(postService.addPost(PostDto,email,img_post));
    }

    @Operation(summary = "상품 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "NULL 관련 문제 발생"),
            @ApiResponse(responseCode = "400", description = "상품 게시글 삭정 중 문제 발생"),
            @ApiResponse(responseCode = "403", description = "접근자와 게시글 작성자가 다릅니다")
    })

    @DeleteMapping("/{post_id}/{email}")
    public ResponseEntity<SuccessRes> deletePost(@PathVariable("email") String email, @PathVariable("post_id") Long postId) throws IOException {
        return ResponseEntity.ok(postService.deletePost(postId,email));
    }

    // 게시글 수정 , email 필요, email 활용 검증 필요
    @Operation(summary = "상품 게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 게시글 변경 성공"),
            @ApiResponse(responseCode = "404", description = "NULL 관련 문제 발생"),
            @ApiResponse(responseCode = "400", description = "상품 게시글 변경 중 문제 발생"),
            @ApiResponse(responseCode = "403", description = "접근자와 게시글 작성자가 다릅니다")
    })
    @Parameters(
            @Parameter(name = "상품 객체", description = "state,Post_id,profileImage 제외한 나머지 속성들")
    )
    @PutMapping("/{post_id}/{email}")
    public ResponseEntity<SuccessRes> changePost(@PathVariable("email") String email,
                                           @PathVariable("post_id") Long postId,
                                           @RequestBody PostDto postDto) throws IOException {
        return ResponseEntity.ok(postService.updatePost(postId,postDto,email));
    }
    // 페이징 형태로 변경
    @Operation(summary = "페이지 별 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기본 화면 page 조회 성공"),
            @ApiResponse(responseCode = "400", description = "상품 게시글 조회 중 문제 발생")
    })

    @GetMapping("/page")
    public ResponseEntity<Page<PostWishListCountDto>> getPostPage(@RequestParam(value = "page",required = false, defaultValue = "1") int page,
                                                                  @RequestParam(value = "nick_name",required = false) String nick_name) {
        return ResponseEntity.ok(postService.findPostPage(page-1,nick_name));
    }

    @Operation(summary = "사용자 상세 페이지 등록 상품 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기본 화면 page 조회 성공"),
            @ApiResponse(responseCode = "400", description = "상품 게시글 조회 중 문제 발생")
    })
    @Parameters({
            @Parameter(name = "nick_name",description = "조회할 상품을 등록한 사용자"),
            @Parameter(name = "page",description = "요청 페이지")
    })
    @GetMapping("/mypage")
    public ResponseEntity<Page<PostWishListCountDto>> getMyPostPage(@RequestParam(value = "page",required = false, defaultValue = "1") int page,@RequestParam("nick_name") String nickName) {
        return ResponseEntity.ok(postService.findMyPostPage(nickName,page-1));
    }
    //게시글 1개 검색
    @Operation(summary = "상품 게시글 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "NULL 관련 문제 발생"),
            @ApiResponse(responseCode = "400", description = "상품 게시글 조회 중 문제 발생")
    })
    @GetMapping("/detail/{post_id}/{email}")
    public ResponseEntity<PostDetailRes> getPost(@PathVariable("post_id") Long postId, @PathVariable("email") String email) {
        return ResponseEntity.ok(postService.findPostDetail(postId,email));
    }

    @Operation(
            operationId = "like",
            description = "좋아요 상품 등록 요청",
            summary = "좋아요"
    )
    @PostMapping("/like/{post_id}/{email}")
    public ResponseEntity<SuccessRes> uploadLike(@PathVariable("post_id") Long postId, @PathVariable("email") String email){
        return ResponseEntity.ok(wishListService.likeRegistration(email, postId));
    }

    @Operation(
            operationId = "like",
            description = "좋아요 상품 조회 요청",
            summary = "좋아요"
    )
    @GetMapping("/profile/like/{nick_name}")
    public ResponseEntity<WishListDto> getLikePost(@PathVariable("nick_name") String nickName){
        return ResponseEntity.ok(wishListService.showLikePost(nickName));
    }

    @Operation(
            operationId = "like",
            description = "좋아요 상품 삭제 요청",
            summary = "좋아요"
    )
    @DeleteMapping("/like/{post_id}/{email}")
    public ResponseEntity<SuccessRes> deleteLikePost( @PathVariable("post_id") Long postId, @PathVariable("email") String email){
        return ResponseEntity.ok(wishListService.delLikePost(email,postId));
    }

    @PostMapping("/payments/sell")
    public PurchaseDto changeState(@RequestBody SellDto sellDto){
        int soldOut=wishListService.sellWishList(sellDto.getPost_id(),sellDto.getEmail());
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

//    @PostMapping("/search")
//    public ResponseEntity<Page<PostDto>> searchFullWord(@RequestBody SearchDto searchDto, @RequestParam(name = "page",required = false,defaultValue = "1") int page){
//        return ResponseEntity.ok(searchService.searchPost(searchDto.getPost_name(), page-1));
//    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostWishListCountDto>> searchFullWord
            (@RequestBody SearchDto searchDto,
             @RequestParam(name = "page",required = false,defaultValue = "1") int page,
             @RequestParam(name = "category_id", required = false, defaultValue = "0") int category_id,
             @RequestParam(name = "gender", required = false, defaultValue = "X") char gender,
            @RequestParam(name = "location", required = false, defaultValue = "X") String location){
        return ResponseEntity.ok(searchService.searchPost(searchDto.getPost_name(), page-1,category_id, gender, location));
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

    //무한스크롤 최초 검색 부
    @GetMapping("/page_post/default")
    public ResponseEntity<Page<PostWishListCountDto>> getDefaultPostPage(@RequestParam(value = "nick_name",required = false) String nick_name) {
        return ResponseEntity.ok(postService.findPostPageInfiniteScroll(0,nick_name,16));
    }

    @GetMapping("/page_post/scroll")
    public ResponseEntity<Page<PostWishListCountDto>> getScrollPostPage(
            @RequestParam(value = "page") int page_number,
            @RequestParam(value = "nick_name",required = false) String nick_name) {
        return ResponseEntity.ok(postService.findPostPageInfiniteScroll(page_number,nick_name,8));
    }

    @GetMapping("/notices")
    public ResponseEntity<JsonNode> getNotices() throws IOException {
        //공지사항 전달 부 입니다.
        //정적인걸 전달하는걸 service로 분리해서 의존성을 하나 올리는 것보다
        //실제 동작 없이 파싱만 진행하므로 서비스 부 분리 없이 controller부에서 즉시 작성해 보았습니다.
        ClassPathResource resource = new ClassPathResource("notice.json");
        InputStream inputStream = resource.getInputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode notices = objectMapper.readTree(inputStream);

        String baseUrl = "/thumbnailfinal.jpg";
        for (JsonNode notice : notices) {
            ((ObjectNode) notice).put("thumbnail", baseUrl);
        }
        return ResponseEntity.ok(notices);
    }
}

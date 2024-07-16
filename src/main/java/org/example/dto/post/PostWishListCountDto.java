package org.example.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class PostWishListCountDto {
    private Long post_id;
    private String nick_name;
    private String post_name;
    private int price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_at;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_at;
    private String image_post;
    private String post_info;
    private int total_number;
    private int category_id;
    @JsonProperty("user_profile")
    private String userProfile;
    private int state;
    private String email;
    private int wish_count;
    private String location;

    @Builder
    public PostWishListCountDto(Long post_id, String nick_name, String post_name, int price, LocalDate start_at, LocalDate end_at, String image_post, String post_info, int total_number, int category_id, String userProfile, int state, String email, long wish_count, String location) {
        this.post_id = post_id;
        this.nick_name = nick_name;
        this.post_name = post_name;
        this.price = price;
        this.start_at = start_at;
        this.end_at = end_at;
        this.image_post = image_post;
        this.post_info = post_info;
        this.total_number = total_number;
        this.category_id = category_id;
        this.userProfile = userProfile;
        this.state = state;
        this.email = email;
        this.wish_count = (int) wish_count;
        this.location = location;
    }


    @Builder
    public PostWishListCountDto(Long post_id, String nick_name, String post_name, int price, LocalDate start_at, LocalDate end_at, String image_post, String post_info, int total_number, int category_id, String userProfile, int state, String email, int wish_count, String location) {
        this.post_id = post_id;
        this.nick_name = nick_name;
        this.post_name = post_name;
        this.price = price;
        this.start_at = start_at;
        this.end_at = end_at;
        this.image_post = image_post;
        this.post_info = post_info;
        this.total_number = total_number;
        this.category_id = category_id;
        this.userProfile = userProfile;
        this.state = state;
        this.email = email;
        this.wish_count = wish_count;
        this.location = location;
    }
    public static PostWishListCountDto fromPostDto(PostDto postDto,int wishlistCnt) {
        return PostWishListCountDto.builder()
                .post_id(postDto.getPost_id())
                .post_name(postDto.getPost_name())
                .price(postDto.getPrice())
                .email(postDto.getEmail())
                .image_post(postDto.getImage_post())
                .post_info(postDto.getPost_info())
                .total_number(postDto.getTotal_number())
                .state(postDto.getState())
                .category_id(postDto.getCategory_id())
                .start_at(postDto.getStart_at())
                .end_at(postDto.getEnd_at())
                .nick_name(postDto.getNick_name())
                .userProfile(postDto.getUserProfile())
                .wish_count(wishlistCnt)
                .location(postDto.getLocation())
                .build();
    }
}

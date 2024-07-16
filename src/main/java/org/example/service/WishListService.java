package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.wish_list.EmailDto;
import org.example.dto.wish_list.WishListDto;
import org.example.dto.SuccessRes;
import org.example.entity.WishList;
import org.example.entity.Post;
import org.example.repository.WishListRepository;
import org.example.repository.PostRepository;
import org.example.service.member.MemberFeign;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListService {

    private final WishListRepository wishListRepository;
    private final PostRepository postRepository;
    private final MemberFeign memberFeign;
    @Transactional
    public SuccessRes likeRegistration(String email, Long postId){
        Post post = postRepository.findByPostId(postId);
        if (post.getState()==-1 ||post.getState()==0){return SuccessRes.builder().message("해당 상품이 없습니다").build();}
        else {
            WishList wishList = WishList.builder()
                    .email(email)
                    .post(post)
                    .build();
            wishListRepository.save(wishList);
            return SuccessRes.builder()
                    .message("등록 성공")
                    .postName(post.getPostName())
                    .build();
        }
    }
    public WishListDto showLikePost(String nickName){
        Optional<EmailDto> email = memberFeign.getEmail(nickName);
        if (email.get().getEmail()==null){return WishListDto.builder().message("존재하지 않는 회원입니다").build();}

        log.info(email.get().getEmail());
        Optional<List<WishList>> likePosts = wishListRepository.findAllByEmail(email.get().getEmail());
        likePosts.orElseThrow();
        List<Post> Posts = likePosts.get().stream().map(WishList::getPost).toList();
        return WishListDto.builder()
                .message("등록 상품 조회")
                .likePosts(Posts)
                .build();
    }

    @Transactional
    public SuccessRes delLikePost(String email,Long postId){
        Post post = postRepository.findByPostId(postId);
        if (post.getState()==-1 ||post.getState()==0){
            return SuccessRes.builder()
                    .postName(post.getPostName())
                    .message("기간이 만료되거나 신청불가능한 수업입니다.")
                    .build();
        }
        wishListRepository.deleteByEmailAndPost(email,post);
        return SuccessRes.builder()
                .postName(post.getPostName())
                .message("좋아요 등록 수업 삭제 성공")
                .build();

    }

    @Transactional
    public int sellWishList(List<Long> postIds,String email){
        List<Post> posts = postRepository.findByPostIdIn(postIds).stream()
                .filter(p->p.getState()==-1 ||p.getState()==0)
                .toList();
        log.info(postIds.toString());

        for (Post Post : posts){
            wishListRepository.deleteByEmailAndPost(email,Post);
        }
        return posts.size();
    }
    @Transactional
    public void successPay(List<Long> postIds){
        List<Post> posts=postRepository.findByPostIdIn(postIds);
        wishListRepository.deleteByPostIn(posts);

    }
}

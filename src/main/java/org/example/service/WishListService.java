package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.post.PostDto;
import org.example.dto.wish_list.EmailDto;
import org.example.dto.wish_list.WishListDto;
import org.example.dto.SuccessRes;
import org.example.entity.WishList;
import org.example.entity.Post;
import org.example.repository.WishListRepository;
import org.example.repository.PostRepository;
import org.example.service.member.MemberFeign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<PostDto> showLikePost(String nickName, int page){
        Pageable pageable;
        if(page==0) {pageable = PageRequest.of(page, 16, Sort.by(Sort.Direction.ASC, "wishListId"));}
        else{pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.ASC, "wishListId"));}
        Optional<EmailDto> email = memberFeign.getEmail(nickName);
        log.info(email.get().getEmail());
        Page<Post> likePosts = wishListRepository.findAllByEmail(pageable,email.get().getEmail()).map(WishList::getPost);
        Page<PostDto> posts = likePosts.map(PostDto::ToDto);
        posts.forEach(p->p.setLike(true));
        return posts;
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
                .filter(p -> p.getState()==0)
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

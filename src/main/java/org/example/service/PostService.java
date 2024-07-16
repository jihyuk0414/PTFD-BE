package org.example.service;

import org.example.annotation.TimeCheck;
import org.example.dto.SuccessRes;
import org.example.dto.post.PostDetailRes;
import org.example.dto.post.PostDto;
import org.example.dto.post.PostForMessage;
import org.example.dto.post.PostWishListCountDto;
import org.example.dto.wish_list.EmailDto;
import org.example.entity.Post;
import org.example.entity.WishList;
import org.example.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.WishListRepository;
import org.example.service.member.MemberFeign;
import org.example.service.storage.NcpStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository ;
    private final WishListRepository wishListRepository;
    private final MemberFeign memberFeign;
    private final NcpStorageService ncpStorageService;

    public SuccessRes addPost(PostDto postDto, String email, MultipartFile img_Post) throws IOException {
        Optional<String> nickName= memberFeign.getNickName(email);
        Optional<String> profile = memberFeign.getProfile(email);
        nickName.orElseThrow();
        profile.orElseThrow();
        postDto.setNick_name(nickName.get());
        postDto.setUserProfile(profile.get());
        String Post_file_name = ncpStorageService.imageUpload(img_Post);
        postDto.setImage_post(Post_file_name);
        Post post = Post.ToEntity(postDto,email);
        postRepository.save(post);
        return new SuccessRes(post.getPostName(),"success");
    }

    @Transactional
    public Page<PostWishListCountDto> findPostPage (int page,String nickName){
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "postId"));
        Page<Post> postPage = postRepository.findAll(pageable);
        Page<PostDto> posts=postPage.map(PostDto::ToDto);
        Page<PostWishListCountDto> postWishListCountDtos = posts.map(post -> {
            int wishlistCnt = 0;
            if (nickName != null) {
                Optional<EmailDto> email = memberFeign.getEmail(nickName);
                email.orElseThrow();
                List<PostDto> wishs = wishListRepository.findAllByEmail(email.get().getEmail()).get().stream().map(WishList::getPost).toList()
                        .stream().map(PostDto::ToDto).toList();
                posts.forEach(p -> p.setLike(wishs.contains(p)));
            }
            wishlistCnt= wishListRepository.countByPost_PostId(post.getPost_id());
            log.info("postid"+post.getPost_id());
            log.info(""+wishlistCnt);

            return PostWishListCountDto.fromPostDto(post, wishlistCnt) ;
        });

        return postWishListCountDtos;
    }

    @Transactional
    public Page<PostWishListCountDto> findMyPostPage (String nickName,int page){
        Pageable pageable = PageRequest.of(page,8, Sort.by(Sort.Direction.ASC, "postId"));
        Page<PostWishListCountDto> PostPage = postRepository.findAllByNickName(pageable,nickName);
        return PostPage;
    }

    @Transactional
    public SuccessRes deletePost(Long PostId, String email) throws IOException {
            Post Post = postRepository.findByPostId(PostId);
            if (Post.getEmail().equals(email)) {
                ncpStorageService.imageDelete(PostId);
                postRepository.delete(Post);
                return new SuccessRes(Post.getPostName(), "삭제 성공");
            }
            else {
                return new SuccessRes(Post.getPostName(), "등록한 이메일과 일치하지 않습니다.");
            }


    }
    @TimeCheck
    @Transactional
    public PostDetailRes findPostDetail(Long postId,String email)
    {
        Post selectedPost = postRepository.findByPostId(postId);
        // 아래 null 값 반환을 빈 객체로 변경
        if (selectedPost.getState()==-1||selectedPost.getState()==0){return new PostDetailRes();}
        else {
            String keywords = selectedPost.getPostName();
            // 해당 상품의 명을 확인합니다.
            Map<Post, Integer> resultMap = new HashMap<>();
            String[] words = keywords.split(" ");
            // 해당 상품명을 띄어쓰기 기준 분할합니다.
            for (String word : words) {
                List<Post> similarPosts = postRepository.findByPostNameKeyword(word,postId);
                similarPosts.forEach(p->resultMap.put(p,resultMap.getOrDefault(p,0)+1));
            }
            List<Post> postList = new ArrayList<>(resultMap.keySet());
            postList.sort((o1,o2)->resultMap.get(o2).compareTo(resultMap.get(o1)));
            List<Post> topPosts = postList.stream().limit(Math.min(postList.size(),9)).toList();
            PostDetailRes PostDetailRes = new PostDetailRes();
            PostDetailRes.setMe(selectedPost.getEmail().equals(email));
            if (topPosts.isEmpty()) {
                List<Post> categoryPostList = postRepository.findByPostCategory(selectedPost.getCategoryId(), postId,PageRequest.of(0,9)) ;
                PostDetailRes.setPost(selectedPost);
                PostDetailRes.setPostList(categoryPostList);
            }
            else {
                PostDetailRes.setPost(selectedPost);
                PostDetailRes.setPostList(topPosts);
            }
            return PostDetailRes;
            // builder 패턴으로 객체 생성 코드 변경
        }
    }

    @Transactional
    public SuccessRes updatePost(Long postId, PostDto postDto,String email) throws IOException {
        Post post=postRepository.findByPostIdWithLock(postId); //이떄만 lock 메소드 활용했습니다.
        if (post.getState()==-1 ||post.getState()==0){return new SuccessRes("","해당 상품이 없습니다");}
        else {
            if (post.getEmail().equals(email)){
                postRepository.updatePost(postId,postDto.getPost_name(),postDto.getPrice(),
                        postDto.getCategory_id(), postDto.getEnd_at(), post.getImagePost(), post.getPostInfo());
                return new SuccessRes(post.getPostName(),"수정 성공");
            }
            else {return new SuccessRes(post.getPostName(),"등록한 이메일과 일치하지않습니다.");}
        }
    }

    @Transactional
    public void changeState(List<Long> postIds){
        for (Long postId : postIds){
            Post post = postRepository.findByPostId(postId);
            if(post.getTotalNumber()-1>0){ postRepository.updateTotalNumber(post.getTotalNumber()-1,postId);}
            else{
                postRepository.updateTotalNumber(0,postId);
                postRepository.updateState(-1,postId);
            }
        }
    }

    @Transactional
    public PostForMessage sendReservation(Long postId){
        log.info("구매 상품 전송 로직");
        PostForMessage post=postRepository.findImagePostAndPostNameByPostId(postId);
        log.info(post.getPost_name());
        return post;
    }

    //무한스크롤 조회 부
    @Transactional
    public Page<PostWishListCountDto> findPostPageInfiniteScroll (int page,String nickName,int pagesize){
        Pageable pageable = PageRequest.of(page, pagesize, Sort.by(Sort.Direction.ASC, "postId"));
        Page<Post> postPage = postRepository.findAll(pageable);
        Page<PostDto> posts=postPage.map(PostDto::ToDto);
        Page<PostWishListCountDto> postWishListCountDtos = posts.map(post -> {
            int wishlistCnt = 0;
            if (nickName != null) {
                Optional<EmailDto> email = memberFeign.getEmail(nickName);
                email.orElseThrow();
                List<PostDto> wishs = wishListRepository.findAllByEmail(email.get().getEmail()).get().stream().map(WishList::getPost).toList()
                        .stream().map(PostDto::ToDto).toList();
                posts.forEach(p -> p.setLike(wishs.contains(p)));
            }
            wishlistCnt= wishListRepository.countByPost_PostId(post.getPost_id());
            log.info("postid"+post.getPost_id());
            log.info(""+wishlistCnt);

            return PostWishListCountDto.fromPostDto(post, wishlistCnt) ;
        });

        return postWishListCountDtos;
    }


}
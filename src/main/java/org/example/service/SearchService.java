package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.TimeCheck;

import org.example.dto.post.PostDto;
import org.example.dto.wish_list.EmailDto;
import org.example.entity.Post;
import org.example.entity.WishList;
import org.example.repository.PostRepository;
import org.example.repository.WishListRepository;
import org.example.service.member.MemberFeign;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final PostRepository postRepository;
    private final MemberFeign memberFeign;
    private final WishListRepository wishListRepository;

    @TimeCheck
    public List<String> autoComplete(String word) {
        log.info(word);
        return postRepository.findByPostName(word).stream()
                .map(Post::getPostName)
                .toList();

    }

    @TimeCheck
    public Page<PostDto> searchPost(String postName, int page, List<Integer> category_id, List<String> location,String nickName) {
        page = (page == 0) ? 0 : page+1;
        int pageSize = (page == 0) ? 16 : 8;
        //offset으로 시도해 보았으나,jpql 사용 x시 쿼리가 너무너무 길어짐
        log.info("nickname="+nickName);
        log.info("category_id="+category_id);
        log.info("location="+location);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "postId"));
        Page<Post> postPage;

        if(category_id == null && location == null)
        {
            //둘다 null일땐,일반 조회합니다.
            postPage = postRepository.findByPostNamePage(postName,pageable);
        }else {
            //category, location 중 하나라도 null이 아니라면, filter query를 사용합니다.
            postPage = postRepository.findPostsByCategoryAndLocation(postName,category_id,location,pageable);
        }
        //주로 삼항연산자를 사용하시지만, 조금이라도 메소드를 편하게 보게 하고 싶어 이렇게 메소드 구성했습니다.

        Page<PostDto> posts= postPage.map(PostDto::ToDto);
        if (nickName!=null) {
            Optional<EmailDto> email = memberFeign.getEmail(nickName);
            List<PostDto> wishs = wishListRepository.findAllByEmail(email.get().getEmail()).get().stream().map(WishList::getPost).toList()
                    .stream().map(PostDto::ToDto).toList();
            posts.forEach(p -> p.setLike(wishs.contains(p)));
        }
        return posts;

    }//변경을 통해, findposts는 제외했습니다.


}

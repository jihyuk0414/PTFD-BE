package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.TimeCheck;

import org.example.dto.post.PostWishListCountDto;
import org.example.entity.Post;
import org.example.repository.PostRepository;
import org.example.service.member.MemberFeign;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final PostRepository postRepository;
    private final MemberFeign memberFeign;

    @TimeCheck
    public List<String> autoComplete(String word) {
        log.info(word);
        return postRepository.findByPostName(word).stream()
                .map(Post::getPostName)
                .toList();

    }
//    @TimeCheck
//    public Page<PostDto> searchPost(String postName,int page){
//        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.ASC, "postName"));
//        Page<Post> Posts=postRepository.findByPostNameAndStateOrderByCreateAtDesc(postName,pageable);
//        List<PostDto> p  = Posts.stream().map(PostDto::ToDto).toList();
//        log.info(p.toString());
//        return new PageImpl<>(p);
//    }

    @TimeCheck
    public Page<PostWishListCountDto> searchPost(String postName, int page, int category_id, char gender, String location) {
        int pageSize = (page == 0 ? 16 : 8);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "postName"));

        List<PostWishListCountDto> resultList = findMorePosts(postName, category_id, location, pageable);

        // gender가 'X'가 아닌 경우 필터링 ! msa끼리 통신으로 가져오기
        if (gender != 'X') {
            List<String> emaillist = memberFeign.getEmailListByGender(gender);
            log.info(emaillist.toString());
            resultList = resultList.stream()
                    .filter(dto -> emaillist.contains(dto.getEmail()))
                    .collect(Collectors.toList());
        }
        log.info(resultList.toString());
        log.info(String.valueOf(resultList.size()));

        int start ;
        int end ;
        if (page == 0)
        {
            start = 0;
            end = Math.min(11,resultList.size());
        }
        else {
            start = 16 + ( (page -1)*pageSize);
            end = Math.min(start+pageSize-1, resultList.size());
        } // 만든, page 번호 잘라내기 입니다.


        List<PostWishListCountDto> pageContent = resultList.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, pageSize), resultList.size());
    }

    private List<PostWishListCountDto> findMorePosts(String postName, int category_id, String location, Pageable pageable) {
        if (category_id == 0 && location.equals("X")) {
            return postRepository.findPostPageByPostName(postName);
        } else if (category_id == 0) {
            return postRepository.findByPostNameAndStateAndLocationOrderByCreateAtDesc(postName, location);
        } else if (location.equals("X")) {
            return postRepository.findByPostNameAndStateAndCategoryIdOrderByCreateAtDesc(postName, category_id);
        } else {
            return postRepository.findByPostNameAndStateAndLocationAndCateogryIdOrderByCreateAtDesc(postName, location, category_id);
        }
    }
}

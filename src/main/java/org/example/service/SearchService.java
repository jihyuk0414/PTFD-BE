package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.TimeCheck;

import org.example.dto.post.PostDto;
import org.example.dto.post.PostWishListCountDto;
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
import java.util.stream.Collectors;

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
    public Page<PostDto> searchPost(String postName, int page, int category_id, String location,String nickName) {
        if (page == 0){
            Pageable pageable = PageRequest.of(page, 16, Sort.by(Sort.Direction.ASC, "postName"));
            List<Post> postsName = postRepository.findAllByPostName(postName);

            List<PostDto> posts=findMorePosts(postsName,category_id,location);
            if (nickName!=null) {
                Optional<EmailDto> email = memberFeign.getEmail(nickName);
                List<PostDto> wishs = wishListRepository.findAllByEmail(email.get().getEmail()).get().stream().map(WishList::getPost).toList()
                        .stream().map(PostDto::ToDto).toList();
                posts.forEach(p -> p.setLike(wishs.contains(p)));
            }
            List<PostDto> pageContent = posts.subList(0, Math.min(11,posts.size()));
            return new PageImpl<>(pageContent, PageRequest.of(page, 16), posts.size());
        }
        else{
            Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.ASC, "postName"));
            List<Post> postsName = postRepository.findAllByPostName(postName);

            List<PostDto> posts=findMorePosts(postsName,category_id,location);
            if (nickName!=null) {
                Optional<EmailDto> email = memberFeign.getEmail(nickName);
                List<PostDto> wishs = wishListRepository.findAllByEmail(email.get().getEmail()).get().stream().map(WishList::getPost).toList()
                        .stream().map(PostDto::ToDto).toList();
                posts.forEach(p -> p.setLike(wishs.contains(p)));
            }
            int start = 16 + ( (page)*8);
            List<PostDto> pageContent = posts.subList(start, Math.min(start+8-1, posts.size()));
            return new PageImpl<>(pageContent, PageRequest.of(page, 8), posts.size());
        }
    }

    private List<PostDto> findMorePosts( List<Post> postsName,int category_id, String location) {
        if (category_id == 0 && location.equals("X")) {
            return postsName.stream().map(PostDto::ToDto).toList();
        } else if (category_id == 0) {
            return postsName.stream().map(PostDto::ToDto)
                    .filter(p->p.getLocation().equals(location))
                    .toList();
        } else if (location.equals("X")) {
            return postsName.stream().map(PostDto::ToDto)
                    .filter(p->p.getCategory_id()==category_id)
                    .toList();
        } else {
            return postsName.stream().map(PostDto::ToDto)
                    .filter(p-> p.getLocation().equals(location))
                    .filter(p->p.getCategory_id()==category_id)
                    .toList();
        }
    }
}

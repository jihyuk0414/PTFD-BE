package org.example.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Post;
import org.example.repository.PostRepository;
import org.example.service.PostService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {
    private final PostRepository postRepository;

    private final PostService postService;

    @Transactional
    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void DataCleanAtInit() throws IOException {
        int counttuple = postRepository.countTuple();
        log.info("data cleaning at init.."); //한글이 자꾸 깨져서 보기 쉽게 영어로 했습니다.

        postRepository.updatePostsStateForExpiredPosts();

        if(counttuple > 100)
        {
            List<Post> deletePostList = postRepository.findPostsExpiredOrSelled();
            for (Post deletePost : deletePostList) {
                postService.deletePost(deletePost.getPostId(), deletePost.getEmail());
            }
            log.info("data more than limit, cleaning start.");
        }

        log.info("data cleaning done");
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") //자정마다 data 정리하는 스케줄러
    public void DataCleanAtMidNight() throws IOException {
        int counttuple = postRepository.countTuple();
        log.info("data cleaning at MidNight..");

        postRepository.updatePostsStateForExpiredPosts();

        if(counttuple > 100)
        {
            List<Post> deletePostList = postRepository.findPostsExpiredOrSelled();
            for (Post deletePost : deletePostList) {
                postService.deletePost(deletePost.getPostId(), deletePost.getEmail());
            }
            log.info("data more than limit, cleaning start.");
        }
        log.info("data cleaning done");
    }

}


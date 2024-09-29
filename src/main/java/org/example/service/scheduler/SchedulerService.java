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
public class SchedulerService {
    private final PostRepository postRepository;

    private final PostService postService;

    @Transactional
    @Scheduled(fixedRate = Long.MAX_VALUE)
    public void DataCleanAtInit() throws IOException {
        int counttuple = postRepository.countTuple();

        postRepository.updatePostsStateForExpiredPosts();

        if(counttuple > 100)
        {
            List<Post> deletePostList = postRepository.findPostsExpiredOrSelled();
            for (Post deletePost : deletePostList) {
                postService.deletePost(deletePost.getPostId(), deletePost.getEmail());
            }
        }

    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") //자정마다 data 정리하는 스케줄러
    public void DataCleanAtMidNight() throws IOException {
        int counttuple = postRepository.countTuple();

        postRepository.updatePostsStateForExpiredPosts();

        if(counttuple > 100)
        {
            List<Post> deletePostList = postRepository.findPostsExpiredOrSelled();
            for (Post deletePost : deletePostList) {
                postService.deletePost(deletePost.getPostId(), deletePost.getEmail());
            }
        }
    }

}


package org.example.service;

import org.example.TestConfig;
import org.example.entity.Follow;
import org.example.entity.Member;
import org.example.repository.follow.FollowRepository;
import org.example.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Import(TestConfig.class)
class FollowServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;
    @Test
    void followReq() {
        String MyEmail = "j6778@naver.com";
        String email ="jj6778@naver.com";
        Member following_member = memberRepository.findByEmail(email).get();
        Member follower_member = memberRepository.findByEmail(MyEmail).get();
        Follow follow = Follow.builder()
                .followingId(following_member.getMemberId())
                .followerId(follower_member.getMemberId())
                .build();

        followRepository.save(follow);
        memberRepository.updateFollower(follower_member);
        memberRepository.updateFollowing(following_member);
        System.out.println();
    }
    @Test
    void follower(){
        Optional<Member> member = memberRepository.findByEmail("j6778@naver.com");
        List<Member> folloingList=followRepository.findFollowing(member.get().getNickName());
        folloingList.forEach(s->System.out.println(s.getUserName()));
    }


}
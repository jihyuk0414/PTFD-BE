package org.example.repository.member;

import jakarta.validation.constraints.AssertTrue;
import org.example.TestConfig;
import org.example.repository.follow.FollowRepository;
import org.example.repository.member.MemberRepository;
import org.example.repository.member.MemberRepositoryImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class MemberRepositoryImplTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;


    @Test
    void findId() {
        System.out.println(memberRepository.findId("jj6778@naver.com"));
    }
    @Test
    void followreq(){

        //followRepository.findByFollowingId();
    }
}
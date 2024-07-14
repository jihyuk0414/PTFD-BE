package org.example.service;

import org.example.TestConfig;
import org.example.entity.Member;
import org.example.repository.follow.FollowRepository;
import org.example.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;

    @Test
    void profile(){
        Member member=memberRepository.findByNickName("jaehak").get();
        System.out.println(member.getUserName());
    }
    @Test
    void search(){
        List<String> member_list = memberRepository.findMemberKeyWord("o");
        member_list.forEach(System.out::println);
    }

}
package org.example.repository.member;

import org.example.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepositoryCustom {

    //void changeProfile(Member members);
    void updateInfo(Member member);
    void updateFollower(Member member);
    void updateFollowing(Member member);
    Long findId(String email);
    void updatePoint(int point,String email);
    List<Member> findAllById(List<Long> ids);
    List<String> findMemberKeyWord(String word);
}

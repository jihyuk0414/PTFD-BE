package org.example.repository.follow;

import org.example.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepositoryCustom {
    List<Member> findFollower(String nickName);
    List<Member> findFollowing(String nickName);
}

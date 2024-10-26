package org.example.repository.follow;

import com.querydsl.core.Tuple;
import org.example.dto.member.MemberFollow;
import org.example.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepositoryCustom {
    List<MemberFollow> findFollower(String nickName);
    List<MemberFollow> findFollowing(String nickName);
}

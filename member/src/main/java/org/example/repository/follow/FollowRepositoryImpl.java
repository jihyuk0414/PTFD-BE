package org.example.repository.follow;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberFollow;
import org.example.entity.Member;
import org.example.entity.QFollow;
import org.example.entity.QMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public List<MemberFollow> findFollower(String nickName) {
        QFollow follow = QFollow.follow;
        QMember member = QMember.member;
        System.out.println("팔로워 멤버");
        return query.from(member).select(Projections.constructor(MemberFollow.class,member.nickName,member.profileImage))
                                .where(member.memberId.in(
                                        JPAExpressions.select(follow.followerId)
                                        .from(member)
                                        .innerJoin(follow).on(member.nickName.eq(nickName))
                                        .where(follow.followingId.eq(member.memberId)))).fetch();
    }

    @Override
    public List<MemberFollow> findFollowing(String nickName) {
        QFollow follow = QFollow.follow;
        QMember member = QMember.member;
        System.out.println("팔로잉 멤버");
        return query.from(member).select(Projections.constructor(MemberFollow.class,member.nickName,member.profileImage))
                .where(member.memberId.in(
                        JPAExpressions.select(follow.followingId)
                                .from(member)
                                .innerJoin(follow).on(member.nickName.eq(nickName))
                                .where(follow.followerId.eq(member.memberId)))).fetch();
    }
}

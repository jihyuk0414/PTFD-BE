package org.example.repository.member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Member;
import org.example.entity.QMember;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory query;

    public MemberRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }





    @Override
    @Transactional
    public void updateInfo(Member member) {
        QMember qMember = QMember.member;
        query.update(qMember)
                .set(qMember.userName, member.getUserName())
                .set(qMember.profileImage, member.getProfileImage())
                .set(qMember.nickName,member.getNickName())

                .where(qMember.email.eq(member.getEmail())).execute();
    }

    @Override
    public void updateFollower(Member member) {
        int num = member.getFollower()+1;
        QMember qMember = QMember.member;
        query.update(qMember)
                .set(qMember.follower,num)
                .where(qMember.email.eq(member.getEmail())).execute();
        log.info(String.valueOf(num));
    }

    @Override
    public void updateFollowing(Member member) {
        QMember qMember = QMember.member;
        int num=member.getFollowing()+1;
        query.update(qMember)
                .set(qMember.following,num)
                .where(qMember.email.eq(member.getEmail())).execute();
        log.info(String.valueOf(num));
    }


    @Override
    public Long findId(String email) {
        QMember member = QMember.member;
        return query.select(member.memberId)
                .from(member)
                .where(member.email.eq(email)).fetchOne();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void updatePoint(int point,String email) {
        QMember qMember = QMember.member;
         query.update(qMember).set(qMember.point,point).where(qMember.email.eq(email)).execute();
    }

    @Override
    public List<Member> findAllById(List<Long> ids) {
        QMember member = QMember.member;

        return query.selectFrom(member).where(member.memberId.in(ids)).fetch();
    }

    @Override
    public List<String> findMemberKeyWord(String word) {
        QMember member = QMember.member;
        return query.select(member.nickName).from(member).where(member.nickName.contains(word)).fetch();
    }

}

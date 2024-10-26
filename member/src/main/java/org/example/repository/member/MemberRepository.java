package org.example.repository.member;

import org.example.dto.chat.ChatMember;
import org.example.dto.purchase.MemberForPay;
import org.example.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickName(String nickName);
    Optional<Member> findEmailByNickName(String nickName);
    boolean existsByEmail(String email);

    @Query("select new org.example.dto.purchase.MemberForPay("+
            "m.point, m.socialType)"+
            "from Member m where m.email= :email")
    Optional<MemberForPay> findPointAndTypeByEmail(@Param("email") String email);

    @Query("SELECT m.point FROM Member m WHERE m.email = :email")
    Integer findPointByEmail(@Param("email") String email);

    @Query("select new org.example.dto.chat.ChatMember("+
            "m.email,m.nickName,m.role,m.profileImage,m.userName)"+
            "from Member m where m.nickName= :nick_name")
    ChatMember findChatMemberByNickName(@Param("nick_name") String nickName);
}

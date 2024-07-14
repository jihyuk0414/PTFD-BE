package org.example.repository.member;

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


    @Query("SELECT DISTINCT m.email FROM Member m WHERE m.gender = :gender")
    List<String> findDistinctNickNamesByGender(@Param("gender") char gender);
}

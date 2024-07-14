package org.example.repository.follow;

import org.example.entity.Follow;
import org.example.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long>, FollowRepositoryCustom {

    boolean existsByFollowingIdAndFollowerId(Long following, Long follower);
}

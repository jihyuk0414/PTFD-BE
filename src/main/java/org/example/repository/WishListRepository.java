package org.example.repository;

import org.example.entity.Post;
import org.example.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {

    Optional<List<WishList>> findAllByEmail(String email);
    void deleteByEmailAndPost(String email,  Post post);
    void deleteByPostIn(List<Post> posts);
    int countByPost_PostId(Long postId);
}

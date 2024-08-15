package org.example.repository;

import jakarta.persistence.LockModeType;

import org.example.dto.mail.PostForMail;
import org.example.dto.post.PostDto;
import org.example.dto.post.PostForChat;
import org.example.dto.post.PostWishListCountDto;
import org.example.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Post p WHERE p.postId = :postId")
    Post findByPostIdWithLock(@Param("postId") Long postId);
    //lock을 update 시만 사용하게 바꿔보았습니다.

    Post findByPostId(Long id);

    @Query("SELECT p FROM Post p WHERE p.postName Like %:post_name% ")
    List<Post> findAllByPostName(@Param("post_name") String postName);

    Post findImagePostAndPostNameByPostId(Long postId);

    @Modifying
    @Query("update Post p set p.postName = :post_name, " +
            "p.price = :price, " +
            "p.imagePost = :image_post, " +
            "p.postInfo = :post_info, " +
            "p.categoryId = :category_id, " +
            "p.endAt = :end_at, " +
            "p.location= :location, "+
            "p.totalNumber= :total_number "+
            "where p.postId = :post_id")
    void updatePost(@Param("post_id") Long postId,
                       @Param("post_name") String postName,
                       @Param("price") int price,
                       @Param("category_id") int categoryId,
                       @Param("end_at") LocalDate expireAt,
                       @Param(("total_number")) int totalNumber,
                       @Param("location") String location,
                       @Param("image_post") String imagePost,
                       @Param("post_info")String postIfo);

    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllByPostName(Pageable pageable,String name);

    Page<Post> findAllByNickName(Pageable pageable, String nickName) ;

    @Query("SELECT p FROM Post p WHERE p.postName Like %:keyword% and p.postId != :post_id")
    List<Post> findByPostNameKeyword(@Param("keyword") String keyword,@Param("post_id") Long postId);
    //제목과 유사한 키워드에 따라서 검색하는 쿼리입니다.


    @Query("SELECT p FROM Post p WHERE p.categoryId = :category_id and p.postId != :post_id")
    List<Post> findByPostCategory(@Param("category_id") int categoryId,@Param("post_id") Long PostId,Pageable pageable);


    @Modifying
    @Query("UPDATE Post p SET p.state = :state WHERE p.postId = :post_id")
    void updateState(@Param("state") int state, @Param("post_id") Long postId) ;

    List<Post> findByPostIdIn(List<Long> postIds);

    void deleteByPostIdIn(List<Long> postIds);

    @Query("SELECT p FROM Post p WHERE p.postName LIKE %:postName%")
    List<Post> findByPostName(@Param("postName") String PostName);

    
    @Modifying
    @Query("UPDATE Post p SET p.totalNumber = :totalNumber where p.postId = :postId")
    void updateTotalNumber(@Param("totalNumber")int totalNumber,@Param("postId")Long postId);

    @Query("select count(*) from Post p ")
    int countTuple() ; //Post 인스턴스 수 세기

    @Modifying
    @Query("UPDATE Post p SET p.state = 0 where p.startAt <= CURRENT_DATE")
    void updatePostsStateForExpiredPosts();

    @Query("Select p FROM Post p WHERE p.state in (-1,0)")
    List<Post> findPostsExpiredOrSelled();

    @Query("select new org.example.dto.mail.PostForMail(" +
            "p.imagePost, p.postName)" +
            "from Post p where p.postId = :post_id")
    PostForMail findImageAndNamePostByPostId(@Param("post_id") Long post_id);

    @Query("select new org.example.dto.post.PostForChat(" +
            "p.imagePost, p.postName, p.userProfile, p.postInfo, p.nickName, p.price)" +
            "from Post p where p.postId = :post_id")
    PostForChat findPostForChatByPostId(@Param("post_id") Long post_id);
}

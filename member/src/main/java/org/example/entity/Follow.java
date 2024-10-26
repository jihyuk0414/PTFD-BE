package org.example.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    private Long followerId;

    private Long followingId;

    @Builder
    public Follow(Long followerId,Long followingId){
        this.followerId=followerId;
        this.followingId=followingId;
    }
}

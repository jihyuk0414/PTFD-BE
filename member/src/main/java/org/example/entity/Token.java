package org.example.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String email;
    private String refreshToken;
    @Builder
    public Token(String email, String refreshToken){
        this.email=email;
        this.refreshToken=refreshToken;
    }
}

package org.example.repository.token;

import org.example.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Long> {
    Token findByRefreshToken(String refreshToken);
    void deleteByEmail(String email);
}

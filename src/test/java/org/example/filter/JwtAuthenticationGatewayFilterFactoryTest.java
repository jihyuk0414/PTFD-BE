package org.example.filter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationGatewayFilterFactoryTest {

    @Test
    void apply() {
        String uri = "http://localhost:8888/user/login";
        String url = uri.substring(0,20);
        System.out.println(url);
    }
}
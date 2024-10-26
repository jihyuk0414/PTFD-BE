package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class MemberApplicationTest {

    @Test
    void contextLoads() {
    }
}
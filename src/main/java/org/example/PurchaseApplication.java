package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
@Slf4j
public class PurchaseApplication {
    public static void main(String[] args) {
        log.info("contest16: this is for proving ci/cd ");
        SpringApplication.run(PurchaseApplication.class, args);
    }
}
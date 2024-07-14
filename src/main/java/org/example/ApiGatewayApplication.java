package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@EnableDiscoveryClient
@Slf4j

public class ApiGatewayApplication {
    public static void main(String[] args) {
        log.info("testcicd");
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
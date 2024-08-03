package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class ChattingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChattingApplication.class,args);
    }
}
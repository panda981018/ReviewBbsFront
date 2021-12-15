package com.example.springsecuritytest;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableBatchProcessing
@EnableRedisHttpSession
@SpringBootApplication
public class ReviewBbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewBbsApplication.class, args);
    }

}

package com.redis.example.pubsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class PubSubApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubSubApplication.class,args);
    }
}

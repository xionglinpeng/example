package com.xlp.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class ExampleRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleRedisApplication.class, args);
    }

}


package com.xlp.example.redis.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TransactionMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TransactionMain.class, args);


    }
}

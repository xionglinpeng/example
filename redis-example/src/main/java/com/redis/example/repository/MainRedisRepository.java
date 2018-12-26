package com.redis.example.repository;

import com.redis.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.UUID;

@SpringBootApplication
@EnableRedisRepositories
public class MainRedisRepository implements ApplicationRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainRedisRepository.class, args);
        SpringApplication.exit(context);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserRedisRepository userRedisRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setName("张三");
        user.setAge(22);
        user.setNickname("存在信号");
        user.setUsername("13980021561");
        user.setPassword("173956@34%~!@");
        userRedisRepository.save(user);
    }
}

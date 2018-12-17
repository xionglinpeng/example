package com.redis.example;

import com.redis.example.entity.User;
import com.redis.example.service.IUserService;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class RedisExampleApplication {



    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RedisExampleApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = app.run(args);

//        Object o= context.getBean(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
//        IUserService userService = context.getBean(IUserService.class);
//        User user = userService.getUser("hello-cacheLock");
//        System.out.println(String.format("user = %s",user));


        StringRedisTemplate redisTemplate = context.getBean(StringRedisTemplate.class);
        System.out.println(redisTemplate);


        SpringApplication.exit(context);
    }
}

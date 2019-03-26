package com.redis.example;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableCaching
public class RedisExampleApplication {

    public static void main(String[] args) {
//        SpringApplication.run(RedisExampleApplication.class,args);

        SpringApplication app = new SpringApplication(RedisExampleApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        ConfigurableApplicationContext context = app.run(args);
//        Object o= context.getBean(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
        SpringApplication.exit(context);
    }
}

package com.redis.example.locks;

import com.redis.example.entity.User;
import com.redis.example.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class MainCacheLock implements ApplicationRunner {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = userService.getUser("hello-cacheLock");
        System.err.println(String.format("user = %s",user));
    }
}

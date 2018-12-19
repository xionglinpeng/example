package com.redis.example.service.impl;

import com.redis.example.entity.User;
import com.redis.example.locks.CacheLock;
import com.redis.example.service.IUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Override
    @CacheLock(cacheNames = "lock",key = "#root.args[0]")
    public User getUser(String userUuid) {
        return new User();
    }
}

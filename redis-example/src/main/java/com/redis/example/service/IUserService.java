package com.redis.example.service;

import com.redis.example.entity.User;

public interface IUserService {

    User getUser(String userUuid);
}

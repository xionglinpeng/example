package com.redis.example.repository;

import com.redis.example.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<User,String> {
}

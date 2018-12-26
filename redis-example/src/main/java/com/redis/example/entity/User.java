package com.redis.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("user")
public class User implements Serializable {

    @Id
    private String uuid;

    private String name;

    private Integer age;

    private String nickname;

    private String username;

    private String password;
}

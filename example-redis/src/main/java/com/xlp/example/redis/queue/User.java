package com.xlp.example.redis.queue;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private String name;

    private String nickname;

    private int age;

    private String phone;
}

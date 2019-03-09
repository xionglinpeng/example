package com.redis.example.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pubsub")
public class PushMessageController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public boolean s(String message){
        redisTemplate.convertAndSend("hello",message);

        return true;
    }
}

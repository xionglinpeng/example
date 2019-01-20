package com.xlp.example.redis.transaction;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@Component
public class NoneTransaction implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(this::execute);
            thread.setName("my thread "+i);
            thread.start();
        }
        //等待500毫秒，让操作由足够的时间完成
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute(){
        String key = "notrans:";
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        //自增操作
        Long increment = valueOperations.increment(key);
        System.out.println(Thread.currentThread().getName()+" : "+increment);
        //休眠100毫秒来放大问题
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //自减操作
        valueOperations.decrement(key);
    }

}

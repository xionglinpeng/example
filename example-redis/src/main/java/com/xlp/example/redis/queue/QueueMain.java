package com.xlp.example.redis.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class QueueMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(QueueMain.class, args);
        RedisTemplate<Object,Object> redisTemplate = context.getBean("redisTemplate",RedisTemplate.class);

        RedisDelayQueue<User> delayQueue = new RedisDelayQueue<>(redisTemplate,"user",QueueMain::callback,1);
        System.out.println(System.currentTimeMillis());

        User user = new User();
        user.setName("王晓楠");
        user.setNickname("爱∞爱");
        user.setAge(18);
        user.setPhone("13980021561");

        delayQueue.add(user,10000);

        User user1 = new User();
        user1.setName("王梦娜");
        user1.setNickname("爱∞爱");
        user1.setAge(20);
        user1.setPhone("13980021561");
        delayQueue.add(user1,15000);

        //因为没有Web服务，运行完成之后会自动关闭容器，so，休眠1分支后退出
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void callback(User user){
        System.out.println(user);
        System.out.println(System.currentTimeMillis());
    }
}

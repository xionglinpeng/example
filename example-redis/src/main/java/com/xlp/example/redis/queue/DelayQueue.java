package com.xlp.example.redis.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
public class DelayQueue<T> {

//    private RedisTemplate<String, String> redisTemplate;

    private RedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    private ZSetOperations<String, String> queueOperations;

    public DelayQueue(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
        this.stringSerializer = redisTemplate.getStringSerializer();
        this.queueOperations = redisTemplate.opsForZSet();
    }

    /**
     * 将消息序列化成一个字符串，作为zset的value
     * @param queueKey
     * @param delayTime
     */
    public void delay(String queueKey,String message,long delayTime){
        long mills = System.currentTimeMillis() + delayTime;
        queueOperations.add(queueKey,message,mills);
    }

    public void loop(Consumer<T> callback){
        //noinspection InfiniteLoopStatement
        do {
            //设置offset为0，count为1，即每次只取一个到期的任务，将其它同时到期的任务分摊给其他线程或进程处理。
            Set<String> tasks = queueOperations.rangeByScore("", 0, System.currentTimeMillis(), 0, 1);
            if (tasks.isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String task = tasks.iterator().next();
            if (queueOperations.remove("", task) == 1) {
                T message = (T) serializer.deserialize(null);
                callback.accept(message);
            }
        } while (true);


    }

    public static void main(String[] args) {

    }


}

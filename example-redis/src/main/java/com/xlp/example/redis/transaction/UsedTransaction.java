package com.xlp.example.redis.transaction;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 1.事务在集群模式不支持。
 * 2.RedisTemplate默认禁用事务。
 *     如果要启用事务，需要设置：redisTemplate.setEnableTransactionSupport(true);
 * 3.事务watch监听，执行失败，会返回空的List集合
 */
@Component
public class UsedTransaction implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String,Integer> redisTemplateLong;


    @Override
    public void run(String... args) throws Exception {
        //实际上在注册为Bean的时候就应该开启其事务
        stringRedisTemplate.setEnableTransactionSupport(true);

//        for (int i = 0; i < 3; i++) {
//            Thread thread = new Thread(this::executeTransaction);
//            thread.setName("(use transaction)my thread "+i);
//            thread.start();
//        }

        watch();
    }

    /**
     * (use transaction)my thread 0 : 1
     * (use transaction)my thread 0 : 0
     * (use transaction)my thread 2 : 1
     * (use transaction)my thread 2 : 0
     * (use transaction)my thread 1 : 1
     * (use transaction)my thread 1 : 0
     */
    public void executeTransaction(){
        String key = "notrans:";
        stringRedisTemplate.multi();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        //自增操作
        valueOperations.increment(key);
        //休眠100毫秒来放大问题
        sleep(100);
        //自减操作
        valueOperations.decrement(key);
        List<Object> exec = stringRedisTemplate.exec();
        exec.forEach(o -> System.out.println(Thread.currentThread().getName()+" : "+o));
    }


    /**
     * (use transaction)my thread 0 : [1, 0]
     * (use transaction)my thread 2 : [1, 0]
     * (use transaction)my thread 1 : [1, 0]
     */
    public void executePiplineTransaction(){
        byte[] key = stringRedisTemplate.getStringSerializer().serialize("notrans:");
        Assert.notNull(key,()->"transaction test 'key' is null.");
        List<Object> exec = stringRedisTemplate.executePipelined((RedisCallback<?>) connection -> {
            connection.multi();
            //自增操作
            connection.incr(key);
            //休眠100毫秒来放大问题
            sleep(100);
            //自减操作
            connection.decr(key);
            connection.exec();
            return null;
        });
        System.out.println(Thread.currentThread().getName()+" : "+exec);
    }

    /**
     * 我开始监视notrans:
     * 修改了notrans:
     * 事务执行失败，休眠50毫秒重试
     * 我开始监视notrans:
     * 事务执行成功，返回结果：[true]
     */
    @SuppressWarnings("all")
    public void watch(){
        String key = "notrans:";
        Thread thread1 = new Thread(()->{
            while (true) {
                stringRedisTemplate.watch(key);
                System.out.println("我开始监视"+key);
                //必须在事务开始之前，监视之后获取值进行计算，因为在事务开始之后获取值，不会实际执行，其结果返回的是null。
                //而在监视之后获取值，是因为如果在获取值之后在进行监视，那么有可能出现获取值之后，监视之前，值就被其他线程或进程修改了。
                BoundValueOperations<String, Integer> valueOperations = redisTemplateLong.boundValueOps(key);
                int value = valueOperations.get();
                stringRedisTemplate.multi();
                sleep(2000);
                valueOperations.set(++value);

//                Random random = new Random();
//                if (random.nextBoolean()){
//                    System.out.println("情况有变，放弃监听"+key);
//                    stringRedisTemplate.discard();
//                }
                System.out.println("开始执行事务");
                List<Object> exec = stringRedisTemplate.exec();
                if (exec.isEmpty()) {
                    System.out.println("事务执行失败，休眠50毫秒重试");
                    sleep(50);
                } else {
                    System.out.println("事务执行成功，返回结果："+exec);
                    break;
                }
            }
        });
        thread1.start();

        Thread thread2 = new Thread(()->{
            //这里休眠时长设大一点，测试初次连接需要消耗较长的时间
            sleep(2000);
            stringRedisTemplate.opsForValue().increment(key);
            System.out.println("修改了"+key);
        });
        thread2.start();
    }

    private void sleep(long timeout){
        try {
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

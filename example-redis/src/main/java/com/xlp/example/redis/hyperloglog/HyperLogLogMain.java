package com.xlp.example.redis.hyperloglog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.jws.Oneway;

@SpringBootApplication
public class HyperLogLogMain {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void basicHyperLogLogOperations(){

        HyperLogLogOperations<String, String> hyperLogLogOperations = stringRedisTemplate.opsForHyperLogLog();

        //pfadd
        for (int i = 0; i < 1000; i++) {
            hyperLogLogOperations.add("{user}:1","usern"+i);
            //pfcount
            Long size = hyperLogLogOperations.size("{user}:1");
            if (size != i+1) {
                System.out.printf("%d %d\n",size,i+1);
                break;
            }
        }
        //99 100

        for (int i = 0; i < 100000; i++) {
            hyperLogLogOperations.add("{user}:2","user"+i);
        }
        Long size = hyperLogLogOperations.size("{user}:2");
        System.out.printf("%d %d\n",100000,size);
        //100000 99725

        Long union = hyperLogLogOperations.union("{user}:3", "{user}:1", "{user}:2");
        System.out.printf("%d\n",union);
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HyperLogLogMain.class, args);
        HyperLogLogMain hyperLogLogMain = context.getBean(HyperLogLogMain.class);
        hyperLogLogMain.basicHyperLogLogOperations();
    }
}

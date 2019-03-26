package com.xlp.example.redis.repositories.pertial_update;

import com.xlp.example.redis.repositories.entity.Address;
import com.xlp.example.redis.repositories.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SimplePartialUpateRunner implements ApplicationRunner {

    @Autowired
    private RedisKeyValueTemplate redisKeyValueTemplate;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        PartialUpdate<Person> update = new PartialUpdate<>("16bbdf07-8876-414f-b28f-f2a65c62b520",Person.class)
                .set("firstname","Tom")
                .set("address.city","Manhattan")
                .del("lastname");
        redisKeyValueTemplate.update(update);

        update = new PartialUpdate<>("16bbdf07-8876-414f-b28f-f2a65c62b520",Person.class)
                .set("address",new Address("America","NewYork"))
                .set("attributes", Collections.singletonMap("nickname","雅诗兰黛"));
        redisKeyValueTemplate.update(update);

        update = new PartialUpdate<>("16bbdf07-8876-414f-b28f-f2a65c62b520",Person.class)
                .refreshTtl(true)
                .set("expiretion",60000L);
        redisKeyValueTemplate.update(update);
    }
}

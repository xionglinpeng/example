package com.xlp.example.redis.repositories.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.Random;

@Data
@TypeAlias("xlp:person")
@RedisHash(value = "{people}",timeToLive = 1000)
@KeySpace("{people:keyspace}")
public class Person {

    @Id
    String id;

    String firstname;

    String lastname;

    Address address;

    @Reference(Person.class)
    private String reference;

    @TimeToLive
    private long expiretion = 120L;

    @TimeToLive
    public long getTimeToLive() {
        long seconds = new Random().nextInt();
        System.out.printf("time to live : %ds\n",seconds);
        return seconds;
    }
}

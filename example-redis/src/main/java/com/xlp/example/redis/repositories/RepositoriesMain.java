package com.xlp.example.redis.repositories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xlp.example.redis.repositories.converters.AddressToBytesConverter;
import com.xlp.example.redis.repositories.converters.AddressToMapConverter;
import com.xlp.example.redis.repositories.converters.BytesToAddressConverter;
import com.xlp.example.redis.repositories.converters.MapToAddressConverter;
import com.xlp.example.redis.repositories.entity.Address;
import com.xlp.example.redis.repositories.entity.Person;
import com.xlp.example.redis.repositories.keyspaces.MyKeyspaceConfiguration;
import com.xlp.example.redis.repositories.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.*;

@SpringBootApplication
@EnableRedisRepositories(keyspaceConfiguration = MyKeyspaceConfiguration.class)
public class RepositoriesMain {

    @Autowired
    private PersonRepository personRepository;


    public void basicCrudOperations(){
        Person person = new Person();
        person.setFirstname("小明");
        person.setLastname("小张");
        person.setAddress(new Address("china","sichuan"));

        Person pers = personRepository.save(person);
        System.out.println(pers);

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Optional<Person> optionalPerson = personRepository.findById(person.getId());
        System.out.println(optionalPerson.get());

        long count = personRepository.count();
        System.out.println(count);

//        personRepository.delete(person);
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RepositoriesMain.class, args);

        RepositoriesMain main = context.getBean(RepositoriesMain.class);
        main.basicCrudOperations();
        System.err.println("执行完毕。。。");
    }
}

package com.xlp.example.redis.repositories;

import com.google.common.collect.Lists;
import com.xlp.example.redis.repositories.entity.Address;
import com.xlp.example.redis.repositories.entity.Person;
import com.xlp.example.redis.repositories.index.MyIndexConfiguration;
import com.xlp.example.redis.repositories.keyspaces.MyKeyspaceConfiguration;
import com.xlp.example.redis.repositories.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@EnableRedisRepositories(keyspaceConfiguration = MyKeyspaceConfiguration.class,
        indexConfiguration = MyIndexConfiguration.class)
public class RepositoriesMain {

    @Autowired
    private PersonRepository personRepository;

    public void basicCrudOperations(){
        //building an Person object
        Person person = new Person();
        person.setFirstname("小明");
        person.setLastname("小张");
        person.setAddress(new Address("china","sichuan"));

        Map<String,String> attributes = new HashMap<>();
        attributes.put("age","20");
        person.setAttributes(attributes);

        Map<String,Person> reletives = new HashMap<>();
        Person person1 = new Person();
        person1.setFirstname("spongeBob");
        reletives.put("person",person1);
        person.setRelatives(reletives);

        ArrayList<Address> addresses = Lists.newArrayList(new Address("china", "chengdu"));
        person.setAddresses(addresses);

        // saving Person object
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

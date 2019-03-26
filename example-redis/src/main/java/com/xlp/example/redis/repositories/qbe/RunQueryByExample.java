package com.xlp.example.redis.repositories.qbe;

import com.xlp.example.redis.repositories.entity.Person;
import com.xlp.example.redis.repositories.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class RunQueryByExample implements ApplicationRunner {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Person person = new Person();
//        person.setFirstname("小明");
        person.setLastname("小张");
        Example<Person> example = Example.of(person);
        Iterable all = personRepository.findAll(example);
        System.out.println(all);
        for (Object anAll : all) {
            System.out.println(anAll);
        }
    }
}

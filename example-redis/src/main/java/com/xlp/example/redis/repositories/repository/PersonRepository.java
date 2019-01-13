package com.xlp.example.redis.repositories.repository;

import com.xlp.example.redis.repositories.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,String> {
}

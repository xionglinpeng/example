package com.xlp.example.redis.repositories.keyspaces;

import com.xlp.example.redis.repositories.entity.Person;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.lang.NonNull;

import java.util.Collections;

public class MyKeyspaceConfiguration extends KeyspaceConfiguration {

    @Override
    protected @NonNull Iterable<KeyspaceSettings> initialConfiguration() {
        return Collections.singleton(new KeyspaceSettings(Person.class,"myPersion123123123"));
    }
}

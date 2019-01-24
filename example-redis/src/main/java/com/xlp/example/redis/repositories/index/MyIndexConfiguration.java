package com.xlp.example.redis.repositories.index;

import org.springframework.data.redis.core.index.IndexConfiguration;
import org.springframework.data.redis.core.index.IndexDefinition;
import org.springframework.data.redis.core.index.SimpleIndexDefinition;
import org.springframework.lang.NonNull;

import java.util.Collections;

public class MyIndexConfiguration extends IndexConfiguration {

    @Override
    @NonNull
    protected Iterable<? extends IndexDefinition> initialConfiguration() {
        System.out.println("loading my index configuration.");
        return Collections.singleton(new SimpleIndexDefinition("people", "firstname"));
    }
}

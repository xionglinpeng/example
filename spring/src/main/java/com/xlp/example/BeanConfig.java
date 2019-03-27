package com.xlp.example;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

public class BeanConfig {

    @Bean
    @Primary
    @Lazy
    @Lookup
    public MessageService messageService(){
        return null;
    }
}

package com.xlp.example.configuration;

import org.springframework.context.annotation.Bean;

public class HelloWorldConfiguration {

    @Bean
    public HelloWorld helloWorld(){
        return new HelloWorld();
    }
}

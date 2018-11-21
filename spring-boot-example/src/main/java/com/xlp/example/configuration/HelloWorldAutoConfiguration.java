package com.xlp.example.configuration;

import org.springframework.context.annotation.Bean;

public class HelloWorldAutoConfiguration {

    @Bean
    public HelloWorld helloWorld(){
        return new HelloWorld();
    }
}

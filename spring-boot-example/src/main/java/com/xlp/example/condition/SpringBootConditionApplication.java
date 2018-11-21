package com.xlp.example.condition;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

public class SpringBootConditionApplication {

    @Bean
    @ConditionalOnSystemProperty(name = "user.name",value = "xlp")
    public String hello(){
        return "Hello World!";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SpringBootConditionApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        String hello = context.getBean("hello", String.class);
        System.out.println("hello : " + hello);
    }
}

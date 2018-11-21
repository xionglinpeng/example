package com.xlp.example;

import com.xlp.example.configuration.EnableHelloWorld;
import com.xlp.example.configuration.HelloWorld;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

//@SpringBootApplication
@EnableHelloWorld
public class SpringBootExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SpringBootExampleApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        HelloWorld bean = context.getBean(HelloWorld.class);
        System.out.println(bean);
    }
}

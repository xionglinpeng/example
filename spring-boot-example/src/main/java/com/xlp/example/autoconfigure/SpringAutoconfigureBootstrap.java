package com.xlp.example.autoconfigure;

import com.xlp.example.configuration.HelloWorld;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class SpringAutoconfigureBootstrap {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = new SpringApplicationBuilder()
                .sources(SpringAutoconfigureBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        HelloWorld helloWorld = context.getBean(HelloWorld.class);

        System.out.println("helloWorld : "+helloWorld);

    }
}

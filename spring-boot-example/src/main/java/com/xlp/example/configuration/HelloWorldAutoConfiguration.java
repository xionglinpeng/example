package com.xlp.example.configuration;

import com.xlp.example.condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//模式注解
@ConditionalOnSystemProperty(name = "user.name",value = "xlp")//条件装配
@EnableHelloWorld//@Enable模块装配
public class HelloWorldAutoConfiguration {//自动装配

}

package com.xlp.example.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public class AfterHelloWorldApplicationContextInitializer<C extends ConfigurableApplicationContext> implements ApplicationContextInitializer<C>,Ordered {

    @Override
    public void initialize(C c) {
        System.out.println("after application initializer : "+ c.getId());
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}

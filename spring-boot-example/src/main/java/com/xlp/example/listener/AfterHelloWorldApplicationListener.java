package com.xlp.example.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public class AfterHelloWorldApplicationListener implements ApplicationListener<ContextRefreshedEvent>,Ordered {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("AfterHelloWorld : "+contextRefreshedEvent.getApplicationContext().getId()+
                ", timestamp : "+contextRefreshedEvent.getTimestamp());
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}

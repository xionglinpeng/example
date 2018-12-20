package com.xlp.example.runListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class HelloWoldRunListener implements SpringApplicationRunListener {

    private static final String PRIFIX = "运行监听器:";

    public HelloWoldRunListener(SpringApplication application,String[] args) {

    }

    @Override
    public void starting() {
        System.out.println(String.format("%s starting.",PRIFIX));
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println(String.format("%s environmentPrepared.",PRIFIX));
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println(String.format("%s contextPrepared.",PRIFIX));
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println(String.format("%s contextLoaded.",PRIFIX));
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println(String.format("%s started.",PRIFIX));
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println(String.format("%s running.",PRIFIX));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println(String.format("%s failed.",PRIFIX));
    }
}

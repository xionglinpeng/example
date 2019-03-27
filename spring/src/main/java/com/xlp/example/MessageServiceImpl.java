package com.xlp.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

public class MessageServiceImpl implements MessageService,
        SmartInitializingSingleton,
        BeanNameAware,
        BeanClassLoaderAware,
        BeanFactoryAware,
        InitializingBean{

    @Override
    public String getMessage() {
        return "Hello World!";
    }

    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("org.springframework.beans.factory.SmartInitializingSingleton");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setBeanName(String name) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}

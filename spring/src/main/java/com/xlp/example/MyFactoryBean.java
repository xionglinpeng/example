package com.xlp.example;

import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBean implements FactoryBean<MessageService> {

    @Override
    public MessageService getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}

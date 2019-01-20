package com.xlp.example.spring;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBean {

    private AmqpTemplate amqpTemplate;
    private AmqpAdmin amqpAdmin;

    @Autowired
    public MyBean(AmqpTemplate amqpTemplate, AmqpAdmin amqpAdmin) {
        this.amqpTemplate = amqpTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    public void test(){
        amqpTemplate.convertAndSend("");
    }

    @RabbitListener(queues = "someQueue")
    public void processMessage(String content){
        //...
    }
}

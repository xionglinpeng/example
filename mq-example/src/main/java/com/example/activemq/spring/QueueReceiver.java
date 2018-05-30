package com.example.activemq.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class QueueReceiver {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        Object msg = jmsTemplate.receiveAndConvert();

        System.out.println("接收到消息："+msg);
    }
}

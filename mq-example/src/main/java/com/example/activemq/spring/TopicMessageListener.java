package com.example.activemq.spring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TopicMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage)message;
            System.out.println("消息监听器监听到消息："+textMessage.getText());
        }catch (JMSException e){
            e.printStackTrace();
        }
    }
}

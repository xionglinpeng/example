package com.example.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class QueueReceiver {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.56.5:61616");
        Connection connection = factory.createConnection();

        Enumeration enumeration = connection.getMetaData().getJMSXPropertyNames();
        while (enumeration.hasMoreElements()){
            System.out.println(enumeration.nextElement());
        }

        connection.start();
        Session session = connection.createSession(Boolean.FALSE,Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createQueue("My-Queue");
        MessageConsumer consumer = session.createConsumer(destination);

//        for (int i = 0; i < 3; i++) {
//            TextMessage message = (TextMessage) consumer.receive();
////            session.commit();
//            message.acknowledge();
//            System.out.println(message.getStringProperty("username"));
//            System.out.println("收到消息："+message.getText());
//            Thread.sleep(1000);
//        }
        TextMessage message = (TextMessage) consumer.receive();
        while (message != null){
            message.acknowledge();
            System.out.println(message.getStringProperty("username"));
            System.out.println("收到消息："+message.getText());
            Thread.sleep(1000);
            message = (TextMessage) consumer.receive();
        }

        session.close();
        connection.close();
    }

}

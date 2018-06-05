package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicNoNPersistentReceiver {

    public static void main(String[] args) throws JMSException{
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.56.2:61616");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("VirtualTopic.Mirror.QUEUE-TEST");

        MessageConsumer consumer = session.createConsumer(destination);

        TextMessage message = (TextMessage) consumer.receive();
        while (message != null){
            System.out.println(message.getText());
            message = (TextMessage)consumer.receive();
        }
        session.close();
        connection.close();

    }
}

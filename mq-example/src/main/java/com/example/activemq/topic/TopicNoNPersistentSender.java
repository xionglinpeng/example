package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;

public class TopicNoNPersistentSender {

    public static void main(String[] args) throws JMSException{

        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);


        Destination destination = session.createTopic("TEXT-TOPIC-NON-PERSISTENT-1");

        MessageProducer producer = session.createProducer(destination);

        TextMessage message = session.createTextMessage(UUID.randomUUID().toString());

        producer.send(message);

        session.commit();
        session.close();
        connection.close();
    }
}

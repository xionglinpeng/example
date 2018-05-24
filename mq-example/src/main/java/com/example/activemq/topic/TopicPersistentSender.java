package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by haolw on 2018/5/24.
 */
public class TopicPersistentSender {

    public static void main(String[] args) throws JMSException{

        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Connection connection = factory.createConnection();

        connection.start();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createTopic("TEXT-TOPIC-PERSISTENT-1");

        MessageProducer producer = session.createProducer(destination);

        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setBoolean("status",true);
        mapMessage.setInt("code",200);
        mapMessage.setString("msg","success");

        producer.send(mapMessage);
    }
}

package com.example.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueSender {

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.56.2:61616");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);


        Destination destination = session.createQueue("My-Queue");

        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 30; i++) {
            TextMessage message = session.createTextMessage("Hello ActiveMQ "+i+".");
            message.setStringProperty("username","xlp");
//            Thread.sleep(1000);
            producer.send(message);
        }
        session.commit();
        session.close();
        connection.close();
    }
}

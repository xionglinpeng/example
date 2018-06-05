package com.example.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;

public class QueueProducer {

    public void sender(String brokerURL,String queueName){
        ConnectionFactory factory = new ActiveMQConnectionFactory(
                brokerURL);
        try(Connection connection = factory.createConnection();
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE)){
            connection.start();

            Queue destination = session.createQueue(queueName);

            MessageProducer producer = session.createProducer(destination);

            for(int i = 0; i < 30; i++){
                TextMessage message = session.createTextMessage(UUID.randomUUID().toString());
                producer.send(message);
            }
            session.commit();
            System.out.println("Queue消息发送完毕");
        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ActiveMQConnectionFactory(
                "failover:(tcp://192.168.56.4:61616)?randomize=true");

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);


        Destination destination = session.createQueue("My-Queue,CONSUMER-CLUSTER-TEST");

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

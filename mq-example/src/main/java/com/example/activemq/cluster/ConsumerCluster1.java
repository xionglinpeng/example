package com.example.activemq.cluster;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ConsumerCluster1 {

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover:(tcp://192.168.56.2:61616,tcp://192.168.56.4:61616,tcp://192.168.56.5:61616)");
        Connection connection = factory.createConnection();
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

        connection.start();
        Destination destination = session.createQueue("Consumer.A.VirtualTopic.Orders");

        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(message->{
            try {
                System.out.println(((TextMessage) message).getText());
                session.commit();
            }catch (JMSException e){
                e.printStackTrace();
            }
        });

    }
}

package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by haolw on 2018/5/24.
 */
public class TopicPersistentReceiver {

    public static void main(String[] args) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Connection connection = factory.createConnection();

        connection.start();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic("TEXT-TOPIC-PERSISTENT-1");
        TopicSubscriber subscriber = session.createDurableSubscriber(topic,"");


    }
}

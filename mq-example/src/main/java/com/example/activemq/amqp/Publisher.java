package com.example.activemq.amqp;

import org.apache.qpid.amqp_1_0.jms.*;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.apache.qpid.amqp_1_0.jms.impl.QueueImpl;
import org.apache.qpid.amqp_1_0.jms.impl.TopicImpl;

import javax.jms.JMSException;

/**
 * Created by haolw on 2018/5/17.
 */
public class Publisher {

    public static void main(String[] args) throws JMSException{
        String username = AmqpUtils.env("ACTIVEMQ_USER", "admin");
        String password = AmqpUtils.env("ACTIVEMQ_PASSWORD", "admin");
        String host = AmqpUtils.env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(AmqpUtils.env("ACTIVEMQ_PORT", "5672"));
//        String destination = AmqpUtils.arg(args, 0, "topic://event");
        String destination = "";

        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host,port,username,password);

        Destination dest = null;
        if(destination.startsWith("topic://"))
            dest = new TopicImpl(destination);
        else
            dest = new QueueImpl(destination);

        Connection connection = factory.createConnection();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        MessageProducer producer = session.createProducer(dest);

        TextMessage message = session.createTextMessage("hello activemq!");

        producer.send(message);

    }
}

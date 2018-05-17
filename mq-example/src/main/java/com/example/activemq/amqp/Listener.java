package com.example.activemq.amqp;

import org.apache.qpid.amqp_1_0.jms.*;
import org.apache.qpid.amqp_1_0.jms.impl.ConnectionFactoryImpl;
import org.apache.qpid.amqp_1_0.jms.impl.QueueImpl;
import org.apache.qpid.amqp_1_0.jms.impl.TopicImpl;

import javax.jms.JMSException;

/**
 * Created by haolw on 2018/5/17.
 */
public class Listener {

    public static void main(String[] args) throws JMSException {

        String username = AmqpUtils.env("ACTIVEMQ_USER", "admin");
        String password = AmqpUtils.env("ACTIVEMQ_PASSWORD", "admin");
        String host = AmqpUtils.env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(AmqpUtils.env("ACTIVEMQ_PORT", "5672"));
//        String destination = AmqpUtils.arg(args, 0, "topic://event");
        String destination = "";
                //连接工厂
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host,port,username,password);

        //目标
        Destination dest = null;
        if(destination.startsWith("topic://")){
            dest = new TopicImpl(destination);
        }else{
            dest = new QueueImpl(destination);
        }

        //连接
        Connection connection = factory.createConnection();
        connection.start();

        //会话
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //消息消费者
        MessageConsumer consumer = session.createConsumer(dest);

        //消息
        Message message = consumer.receive();

        TextMessage textMessage = (TextMessage)message;
        System.out.println(textMessage.getText());
    }
}

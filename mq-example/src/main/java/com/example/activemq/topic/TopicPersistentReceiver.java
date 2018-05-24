package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

/**
 * Created by haolw on 2018/5/24.
 */
public class TopicPersistentReceiver {

    public static void main(String[] args) throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Connection connection = factory.createConnection();

        //设置客户端ID
        connection.setClientID("PERSISTENT_CLIENT_1");

        //会话
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

        //目的地（主题）
        Topic topic = session.createTopic("TEXT-TOPIC-PERSISTENT-1");

        //持久化订阅者（相当于队列中的consumer）
        TopicSubscriber subscriber = session.createDurableSubscriber(topic,"SUB_1");

        connection.start();

        Message message = subscriber.receive();

        while (message != null) {
            MapMessage mapMessage = (MapMessage)message;
            Enumeration enumeration = mapMessage.getMapNames();
            while (enumeration.hasMoreElements()){
                Object object = enumeration.nextElement();
                System.out.println(object+"="+mapMessage.getObject(object.toString()));
            }
            session.commit();
            message = subscriber.receive();
        }
        session.commit();

        session.close();
        connection.close();


    }
}

package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;

/**
 * Created by haolw on 2018/5/24.
 */
public class TopicPersistentSender {


    public void topicPersistentSender(String brokerURL,String topicName){
        this.topicSender(brokerURL,topicName,DeliveryMode.PERSISTENT);
    }

    public void topicNonPersistentSender(String brokerURL,String topicName){
        this.topicSender(brokerURL,topicName,DeliveryMode.NON_PERSISTENT);
    }

    public void topicSender(String brokerURL,String topicName,int DeliveryMode){

        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
        try(Connection connection = factory.createConnection();
            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE))
        {
            connection.start();

            Topic topic = session.createTopic(topicName);

            MessageProducer producer = session.createProducer(topic);

            producer.setDeliveryMode(DeliveryMode);

            for(int i = 0; i < 30; i++){
                TextMessage message = session.createTextMessage(UUID.randomUUID().toString());
                producer.send(message);
            }
            session.commit();
            System.out.println("Topic消息发送完毕");

        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JMSException{

        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.56.2:61616");

        Connection connection = factory.createConnection();

        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

        Topic destination = session.createTopic("TEXT-TOPIC-PERSISTENT-1");

        MessageProducer producer = session.createProducer(destination);

        //数据结果是2，即默认是持久化的
        System.out.println(producer.getDeliveryMode());
        //设置持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        connection.start();

        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setBoolean("status",true);
        mapMessage.setInt("code",200);
        mapMessage.setString("msg","success");

        producer.send(mapMessage);

        session.commit();
        session.close();
        connection.close();

    }
}

package com.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by haolw on 2018/5/24.
 */
public class TopicProducer {


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

            for(int i = 0; i < 3; i++){
                TextMessage message = session.createTextMessage(i+"");
                producer.send(message);
            }
            session.commit();
            System.out.println("Topic消息发送完毕");

        }catch (JMSException e){
            e.printStackTrace();
        }
    }

}

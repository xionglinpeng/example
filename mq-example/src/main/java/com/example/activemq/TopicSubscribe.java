package com.example.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicSubscribe {

    public void subscribe(String brokerURL,String topicName){
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

            Topic destination = session.createTopic(topicName);

            MessageConsumer consumer = session.createConsumer(destination);
            this.consumer(session,consumer);
        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    public void subscribe(String brokerURL,String topicName,String clientId){
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = factory.createConnection();
            connection.setClientID(clientId);

            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(topicName);

            TopicSubscriber subscriber = session.createDurableSubscriber(topic,"SUB-1");

            connection.start();

            this.consumer(session,subscriber);
        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    private void consumer(Session session,MessageConsumer consumer) throws JMSException{
        consumer.setMessageListener(message->{
            try{
                TextMessage textMessage = (TextMessage)message;
                System.out.println(textMessage.getText());
                session.commit();
            }catch (JMSException e){
                e.printStackTrace();
            }
        });
    }
}

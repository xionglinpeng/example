package com.example.activemq;

import lombok.Data;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.jms.*;

@Data
public class TopicSubscribe {

    private String tag;

    public void subscribe(String brokerURL,String topicName){
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                    "tcp://192.168.56.2:61616?jms.useAsyncSend=true");
            factory.setOptimizeAcknowledge(Boolean.TRUE);

            Connection connection = factory.createConnection();
            ((ActiveMQConnection)connection).setProducerWindowSize(102400);
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
//                Thread.sleep(RandomUtils.nextInt(1000,9000));
                TextMessage textMessage = (TextMessage)message;
                System.out.println(this.tag()+textMessage.getText());
                session.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private String tag(){
        return StringUtils.isNotBlank(this.tag)?this.tag+" = ":"";
    }
}

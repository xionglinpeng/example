package com.example.activemq.consumerFeatures;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class RedeliveryPolicy extends ConsumerFeaturesHandler {

    private static final boolean k = false;

    @Override
    protected boolean condition(int tag) {
        return tag == 7;
    }

    @Override
    protected void execute() throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(super.DEFAULT_BROKER_URL_TCP);
        ActiveMQConnection connection = (ActiveMQConnection)factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(super.DEFAULT_TOPIC_NAME);

        org.apache.activemq.RedeliveryPolicy redeliveryPolicy = new org.apache.activemq.RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        factory.setRedeliveryPolicy(redeliveryPolicy);


        if(k){
            MessageProducer producer = session.createProducer(destination);
            Message message = session.createTextMessage("redelivery policy.");
            producer.send(message);
            session.commit();
            session.close();
            connection.close();
        } else {
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(msg -> {
                try {
                    System.out.println(((TextMessage)msg).getText());
                    session.close();
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }

    }
}

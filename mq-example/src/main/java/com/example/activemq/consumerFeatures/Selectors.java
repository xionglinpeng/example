package com.example.activemq.consumerFeatures;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Selectors extends ConsumerFeaturesHandler{

    @Override
    protected boolean condition(int tag) {
        return tag == 6;
    }

    @Override
    protected void execute() throws Exception{
        ConnectionFactory factory = new ActiveMQConnectionFactory(super.DEFAULT_BROKER_URL_TCP);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(super.DEFAULT_TOPIC_NAME);


        MessageConsumer consumer = session.createConsumer(destination,"age >= 20");
        consumer.setMessageListener(message->{
            System.out.println(message);

            try {
                session.commit();
                session.close();
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        MessageProducer producer = session.createProducer(destination);
        Message message = session.createTextMessage("Hello consumer selectors.");
        message.setIntProperty("age",31);
        producer.send(message);
        session.commit();
    }
}

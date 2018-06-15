package com.example.activemq.consumerFeatures;

import com.example.activemq.queue.QueueConsumer;
import lombok.Data;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.RandomStringUtils;

import javax.jms.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 消息分组
 */
public class MessageGroups extends ConsumerFeaturesHandler{

    @Override
    protected boolean condition(int tag) {
        return tag == 5;
    }

    @Data
    class MessageNum {
        int messageCountNum = 30;
        int messageCurrentNum = 0;
        Connection connection;
        Session session;
        void close(BiConsumer<Connection,Session> consumer){
            consumer.accept(connection,session);
        }

        void close() throws JMSException{
            this.messageCurrentNum++;
            if(this.messageCurrentNum == this.messageCountNum){
                this.close((c,s)->{
                    try {
                        c.close();
                        s.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    protected void execute() {
        ConnectionFactory factory = new ActiveMQConnectionFactory(super.DEFAULT_BROKER_URL_TCP);
        try
        {
            Connection connection = factory.createConnection();
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            connection.start();

            Destination destination = session.createQueue(super.DEFAULT_TOPIC_NAME);

            MessageNum messageNum = new MessageNum();
            messageNum.setConnection(connection);
            messageNum.setSession(session);

            for (int i = 0; i < 3; i++) {
                MessageConsumer consumer = session.createConsumer(destination);
                int j = i;
                consumer.setMessageListener(message->{
                    try {
                        TextMessage text = (TextMessage)message;
                        String groupId = text.getStringProperty("JMSXGroupId");
                        System.out.println("consumer "+j+" => "+groupId+" => message = " + text.getText());
                        session.commit();
                        messageNum.close();
                    }catch (JMSException e){
                        e.printStackTrace();
                    }
                });
            }


            MessageProducer producer = session.createProducer(destination);
            for (int i = 0; i < 30; i++) {
                TextMessage message = session.createTextMessage(RandomStringUtils.randomAlphabetic(10).toUpperCase());
                String JMSXGroupID;
                if(i < 3){
                    JMSXGroupID = "GROUP001";
                } else if (i < 25) {
                    JMSXGroupID = "GROUP002";
                } else {
                    JMSXGroupID = "GROUP003";
                }
                message.setStringProperty("JMSXGroupID",JMSXGroupID);
                producer.send(message);
            }
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

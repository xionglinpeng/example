package com.example.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.ProducerInfo;

import javax.jms.*;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class QueueConsumer {

    private static final Integer DEFAULT_CONSUMER_NUM = 2;

    public void consumer(String brokerURL,String queueName){
        this.consumer(brokerURL,queueName,DEFAULT_CONSUMER_NUM,null);
    }

    public void consumer(String brokerURL,String queueName,int consumerNum){
        this.consumer(brokerURL,queueName,consumerNum,null);
    }

    public void consumer(String brokerURL,String queueName,String threadName){
        this.consumer(brokerURL,queueName,DEFAULT_CONSUMER_NUM,threadName);
    }

    public void consumer(String brokerURL,String queueName,int consumerNum,String threadName){
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = factory.createConnection();

            connection.start();
            Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(queueName);

            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    consumerNum,consumerNum,100000,
                    TimeUnit.DAYS,new LinkedBlockingDeque<>(consumerNum),Runnable->new Thread(Runnable));

            for(int i = 0;i<consumerNum;i++){
                final int j = i;
                executor.execute(()->{
                    try {
                        MessageConsumer consumer = session.createConsumer(destination);
                        consumer.setMessageListener(message -> {
                            try {
                                TextMessage textMessage = (TextMessage)message;
                                System.out.println(threadName+"-"+j+" => "+textMessage.getText());
                                session.commit();
                            }catch (JMSException e){
                                e.printStackTrace();
                            }
                        });
                    }catch (JMSException e){
                        e.printStackTrace();
                    }
                });
            }
        }catch (JMSException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.56.2:61616");
        Connection connection = factory.createConnection();

        Enumeration enumeration = connection.getMetaData().getJMSXPropertyNames();
        while (enumeration.hasMoreElements()){
            System.out.println(enumeration.nextElement());
        }

        connection.start();
        Session session = connection.createSession(Boolean.FALSE,Session.CLIENT_ACKNOWLEDGE);
        Destination destination = session.createQueue("My-Queue");
        MessageConsumer consumer = session.createConsumer(destination);

//        for (int i = 0; i < 3; i++) {
//            TextMessage message = (TextMessage) consumer.receive();
////            session.commit();
//            message.acknowledge();
//            System.out.println(message.getStringProperty("username"));
//            System.out.println("收到消息："+message.getText());
//            Thread.sleep(1000);
//        }
        TextMessage message = (TextMessage) consumer.receive();
        while (message != null){
            message.acknowledge();
            System.out.println(message.getStringProperty("username"));
            System.out.println("收到消息："+message.getText());
//            Thread.sleep(1000);
            message = (TextMessage) consumer.receive();
        }

        session.close();
        connection.close();
    }

}

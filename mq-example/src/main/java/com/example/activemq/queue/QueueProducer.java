package com.example.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;

public class QueueProducer {

    public void sender(String brokerURL,String queueName){
        ConnectionFactory factory = new ActiveMQConnectionFactory(
                brokerURL);
        try(Connection connection = factory.createConnection();
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE)){
            connection.start();

            Queue destination = session.createQueue(queueName);

            MessageProducer producer = session.createProducer(destination);

            for(int i = 0; i < 30; i++){
                TextMessage message = session.createTextMessage(UUID.randomUUID().toString());
                producer.send(message);
            }
            session.commit();
            System.out.println("Queue消息发送完毕");
        }catch (JMSException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ActiveMQConnectionFactory(
                "failover:(tcp://192.168.56.4:61616)?randomize=true");

        Connection connection = factory.createConnection();
        connection.start();

        //第一个参数是否开启事务 true开启 ,false不开启事务，如果开启记得手动提交
        //参数二，表示的是签收模式，一般使用的有自动签收和客户端自己确认签收
        Session session = connection.createSession(Boolean.TRUE,Session.AUTO_ACKNOWLEDGE);


        Destination destination = session.createQueue("My-Queue,CONSUMER-CLUSTER-TEST");

        MessageProducer producer = session.createProducer(destination);

        for (int i = 0; i < 30; i++) {
            TextMessage message = session.createTextMessage("Hello ActiveMQ "+i+".");
            message.setStringProperty("username","xlp");
//            Thread.sleep(1000);
            producer.send(message);
        }
        session.commit();
        session.close();
        connection.close();
    }
}

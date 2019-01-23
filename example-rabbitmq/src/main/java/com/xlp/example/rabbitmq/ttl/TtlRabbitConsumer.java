package com.xlp.example.rabbitmq.ttl;

import com.rabbitmq.client.*;

import java.io.IOException;

public class TtlRabbitConsumer {

    private static final String QUEUE = "TTL_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("192.168.56.3");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicConsume(QUEUE,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                //consumerTag, envelope, properties, body
                System.out.println("recv message : "+new String(body));
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });

    }
}

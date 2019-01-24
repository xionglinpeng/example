package com.xlp.example.rabbitmq.ttl;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class TtlRabbitProducer {

    private static final String EXCHANGE = "TTL_EXCHANGE";
    private static final String QUEUE = "TTL_QUEUE";
    private static final String ROUTING_KEY = "TTL_ROUTING_KEY";
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("192.168.56.3");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        AMQP.Exchange.DeclareOk declareOk1 = channel.exchangeDeclare(EXCHANGE, "direct", true, false, null);
        Map<String,Object> queueArgs = new HashMap<>();
        queueArgs.put("x-message-ttl",6000);//设置队列中消息的过期时间，单位毫秒
        queueArgs.put("x-expires",1800000);//设置队列的过期时间，单位毫秒
//        在使用mq的时候出现这个错误
//        406, “PRECONDITION_FAILED - inequivalent arg ‘durable’ for exchange ‘exchange_name’ in vhost ‘/‘: received ‘false’ but current is ‘true’”
//        这个意思是说，exchange的durable已经true了不能改为false。所以使用的时候要注意如果exchange和queue的durable已经定义好了是不能更改的。除非删掉重现来。
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE, true, false, false, queueArgs);
        AMQP.Queue.BindOk bindOk = channel.queueBind(QUEUE, EXCHANGE, ROUTING_KEY);

        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.deliveryMode(2);//持久化消息
        builder.expiration("60000");//设置ttl=60000ms
        AMQP.BasicProperties basicProperties = builder.build();

        channel.basicPublish(EXCHANGE,ROUTING_KEY,basicProperties,"hello ttl.".getBytes());

        connection.close();
    }
}

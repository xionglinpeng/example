package com.xlp.example.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RabbitConsumer {

    public static final String QUEUE_NAME = "queue_demo";

    public static final String IP_ADDRESS = "192.168.56.3";

    public static final int PORT = 5672;//RabbitMQ服务端默认端口号为5672

    public static void main(String[] args) throws Exception{
        //创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        //创建连接
        Connection connection = factory.newConnection(new Address[]{new Address(IP_ADDRESS,PORT)});
        //创建信道
        Channel channel = connection.createChannel();
        //设置客户端最多接收未被ack的消息个数
        channel.basicQos(64);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("recv message: "+new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        channel.basicConsume(QUEUE_NAME,consumer);
        //等待回调函数执行完毕后，关闭资源
        TimeUnit.SECONDS.sleep(5);
        //关闭资源
        channel.close();
        connection.close();
    }
}

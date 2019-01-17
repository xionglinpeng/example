package com.xlp.example.rabbitmq;

import com.rabbitmq.client.*;
import org.omg.CORBA.TRANSACTION_MODE;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RabbitProducer {

    public static final String EXCHANGE_NAME = "exchange_demo";

    public static final String ROUTING_KEY = "routingkey_demo";

    public static final String QUEUE_NAME = "queue_demo";

    public static final String IP_ADDRESS = "192.168.56.3";

    public static final int PORT = 5672;//RabbitMQ服务端默认端口号为5672

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("admin");
        factory.setPassword("admin");
        //创建连接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
        //创建队列
        channel.queueDeclare(QUEUE_NAME, true,false,false,null);
        //将交换器和队列通过路由键绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        //发送消息
        String message = "Hello Wrold!";
//        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());


        //参数
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange","myAe");//备份交换器

        channel.exchangeDeclare("normalExchange","direct",true,false,arguments);
        channel.queueDeclare("normalQueue",true,false,false,null);
        channel.queueBind("normalQueue","normalExchange","normalKey",null);

        channel.exchangeDeclare("myAe","fanout",true,false,null);
        channel.queueDeclare("unroutedQueue",true,false,false,null);
        channel.queueBind("unroutedQueue","myAe","",null);


        channel.basicPublish("normalExchange",ROUTING_KEY,true, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("Basic Return返回的结果是："+message);
            }
        });




        //exchange：
        //type：
        //durable：
        //autoDelete：
        //argument：

        //queue
        //durable
        //exclusive
        //autoDelete
        //arguments



        //关闭资源
//        channel.close();
//        connection.close();
    }

    
}

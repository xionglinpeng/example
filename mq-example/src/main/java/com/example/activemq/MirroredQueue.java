package com.example.activemq;

import com.example.activemq.topic.TopicSubscribe;

/**
 * 测试镜像队列
 */
public class MirroredQueue {

    private static final String BROKER_URL = "failover:(tcp://192.168.56.2:61616)";

    private static final String QUEUE_NAME = "QUEUE-TEST";

    //虚拟主题，默认以`VirtualTopic.Mirror.>`为前缀
    private static final String MIRRORED_TOPIC_NAME = "VirtualTopic.Mirror.QUEUE-TEST";

    public static void main(String[] args) {
        //非持久化订阅，so，先订阅，在发送消息
        TopicSubscribe topicSubscribe = new TopicSubscribe();
        topicSubscribe.subscribe(BROKER_URL,MIRRORED_TOPIC_NAME);

        QueueProducer producer = new QueueProducer();
        producer.sender(BROKER_URL,QUEUE_NAME);
    }
}

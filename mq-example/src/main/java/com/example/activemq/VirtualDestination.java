package com.example.activemq;

import com.example.activemq.queue.QueueConsumer;
import com.example.activemq.topic.TopicPersistentSender;

/**
 * 测试虚拟主题
 */
public class VirtualDestination {

    private static final String BROKER_URL = "tcp://192.168.56.2:61616";

    //虚拟主题，默认前缀`VirtualTopic.>`
    private static final String VIRTUAL_TOPIC_NAME = "VirtualTopic.TEST";

    //虚拟主题队列，默认格式`Consumer.*.VirtualTopic.>`
    private static final String VIRTUAL_QUEUE_NAME_A = "Consumer.A.VirtualTopic.TEST";
    private static final String VIRTUAL_QUEUE_NAME_B = "Consumer.B.VirtualTopic.TEST";


    public static void main(String[] args) {
        QueueConsumer queueConsumer = new QueueConsumer();
        queueConsumer.consumer(BROKER_URL,VIRTUAL_QUEUE_NAME_A,"QUEUE-A");

        queueConsumer.consumer(BROKER_URL,VIRTUAL_QUEUE_NAME_B,"QUEUE-B");

        TopicPersistentSender topicPersistentSender = new TopicPersistentSender();
        topicPersistentSender.topicPersistentSender(BROKER_URL,VIRTUAL_TOPIC_NAME);
    }
}

package com.example.activemq;

import com.example.activemq.queue.QueueProducer;

/**
 * 严格顺序分发策略
 */
public class StrictOrderDispatchPolicy {

//    private static final String BROKER_URL = "tcp://192.168.56.2:61616";
    private static final String BROKER_URL = "tcp://127.0.0.1:61616";

    private static final String TOPIC_NAME = "STRIC-ORDER-DISPATCH-POLICY";

    public static void main(String[] args) {
//        TopicSubscribe topicSubscribe = new TopicSubscribe();
//        topicSubscribe.subscribe(BROKER_URL,TOPIC_NAME);

        TopicSender topicSender = new TopicSender();
        topicSender.topicNonPersistentSender(BROKER_URL,TOPIC_NAME);
//        topicSender.topicNonPersistentSender(BROKER_URL,TOPIC_NAME);
        TopicSender topicSender1 = new TopicSender();
        topicSender1.topicNonPersistentSender(BROKER_URL,TOPIC_NAME);

    }
}

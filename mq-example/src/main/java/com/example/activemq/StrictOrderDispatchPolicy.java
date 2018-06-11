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

        for(int i = 0; i < 2 ; i++){
            TopicSubscribe topicSubscribe = new TopicSubscribe();
            topicSubscribe.setTag("thread-"+i);
            Thread thread = new Thread(()->{topicSubscribe.subscribe(BROKER_URL,TOPIC_NAME);});
            thread.start();
        }


        TopicSender topicSender = new TopicSender();
        Thread thread1 = new Thread(()->topicSender.topicNonPersistentSender(BROKER_URL,TOPIC_NAME));
        Thread thread2 = new Thread(()->topicSender.topicNonPersistentSender(BROKER_URL,TOPIC_NAME));
        thread1.start();
        thread2.start();
    }
}

package com.example.activemq.consumerFeatures;

import com.example.activemq.QueueProducer;
import com.example.activemq.queue.QueueConsumer;

/**
 * Created by haolw on 2018/6/12.
 */
public class ConsumerPriority extends ConsumerFeaturesHandler {

    @Override
    protected boolean condition(int tag) {
        return tag == 2;
    }

    @Override
    protected void execute() {
        String brokerURL = "failover:(tcp://127.0.0.1:61616)";
        String queueName = "CONSUMER-PRIORITY-QUEUE";
        QueueConsumer consumer = new QueueConsumer();
        //最高优先级的消费者收到消息
        consumer.consumer(brokerURL,queueName+"?consumer.priority=11","CONSUMER-FOO");
        consumer.consumer(brokerURL,queueName+"?consumer.priority=100","CONSUMER-BAR");
        consumer.consumer(brokerURL,queueName+"?consumer.priority=50","CONSUMER-ENG");
        consumer.consumer(brokerURL,queueName+"?consumer.priority=126","CONSUMER-SSD");
        consumer.consumer(brokerURL,queueName+"?consumer.priority=10","CONSUMER-HHG");

        QueueProducer producer = new QueueProducer(brokerURL,queueName);
        producer.sender();


    }
}

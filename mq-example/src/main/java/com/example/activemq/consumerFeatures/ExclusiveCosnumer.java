package com.example.activemq.consumerFeatures;

import com.example.activemq.QueueProducer;
import com.example.activemq.queue.QueueConsumer;

/**
 * Created by haolw on 2018/6/12.
 */
public class ExclusiveCosnumer extends ConsumerFeaturesHandler {

    @Override
    protected boolean condition(int tag) {
        return tag == 1;
    }

    @Override
    protected void execute() {
        String brokerURL = "failover:(tcp://127.0.0.1:61616)";
        String queueName = "EXCLUSIVE-CONSUMER-QUEUE?consumer.exclusive=true";
        QueueProducer producer = new QueueProducer(brokerURL,queueName);
        producer.sender();
        QueueConsumer consumer = new QueueConsumer();
        consumer.consumer(brokerURL,queueName);
    }
}

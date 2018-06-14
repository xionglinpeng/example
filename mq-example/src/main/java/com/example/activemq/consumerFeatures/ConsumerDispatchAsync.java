package com.example.activemq.consumerFeatures;

/**
 * Created by haolw on 2018/6/12.
 */
public class ConsumerDispatchAsync extends ConsumerFeaturesHandler{
    @Override
    protected boolean condition(int tag) {
        return tag == 3;
    }

    @Override
    protected void execute() {
        String brokerURL = "failover:(tcp://127.0.0.1:61616)";
        String queueName = "CONSUMER-DISPATCH-ASYNC-QUEUE";

    }
}

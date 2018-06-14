package com.example.activemq.consumerFeatures;

import com.example.activemq.topic.TopicSubscribe;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by haolw on 2018/6/14.
 * 管理持久订阅
 */
public class ManageDurableSubscribers extends ConsumerFeaturesHandler {

    private static final String BROKER_URL = "failover:(tcp://127.0.0.1:61616)";
    private static final String TOPIC_NAME = "";
    private static final String CLIENT_ID = RandomStringUtils.randomAlphanumeric(36);

    @Override
    protected boolean condition(int tag) {
        return tag == 4;
    }

    @Override
    protected void execute() {
        TopicSubscribe subscribe = new TopicSubscribe();
        subscribe.subscribe(BROKER_URL,super.DEFAULT_TOPIC_NAME,CLIENT_ID);

        System.out.println("finish");
    }
}

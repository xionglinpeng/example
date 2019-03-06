package com.redis.example.pubsub;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

import java.util.*;

@SpringBootApplication
public class PubSubApplication {

    public static void main(String[] args) {
//        SpringApplication.run(PubSubApplication.class,args);

        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();

        MessageListener listener = new DefaultMessageListener();


        List<Topic> topics = new ArrayList<>();
        topics.add(new ChannelTopic("hello"));

        Map<MessageListener, Collection<? extends Topic>> listeners = new HashMap<>();
        listeners.put(listener,topics);
        listenerContainer.setMessageListeners(listeners);


    }
}

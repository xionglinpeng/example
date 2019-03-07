package com.redis.example.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.CollectionFactory;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.*;

@Configuration
public class PubSubConfiguration {

//    @Bean
//    public MessageListenerAdapter listenerAdapter(){
//        return new MessageListenerAdapter(new DefaultMessageDelegate());
//    }

    @Bean
    public MessageListener listener(){
        return new DefaultMessageListener();
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory collectionFactory){
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(collectionFactory);

        List<Topic> topics = new ArrayList<>();
        topics.add(new ChannelTopic("hello"));
//        topics.add(new PatternTopic("*"));

        Map<MessageListener, Collection<? extends Topic>> listeners = new HashMap<>();
        listeners.put(listener(),topics);
//        listeners.put(listenerAdapter(),topics);
        listenerContainer.setMessageListeners(listeners);
        return listenerContainer;
    }



}

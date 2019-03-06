package com.redis.example.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class DefaultMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println(message);
        System.out.println(new String(pattern));
    }
}

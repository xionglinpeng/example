package com.redis.example.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class DefaultMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.printf("message = %s\n",message);
        System.out.printf("channel = %s\n",new String(pattern));
    }
}

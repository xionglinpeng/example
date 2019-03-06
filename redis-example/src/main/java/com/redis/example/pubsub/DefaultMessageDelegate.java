package com.redis.example.pubsub;

import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.Serializable;
import java.util.Map;

public class DefaultMessageDelegate implements MessageDelegate {

    @Override
    public void handleMessage(String message) {

    }

    @Override
    public void handleMessage(Map message) {

    }

    @Override
    public void handleMessage(byte[] message) {

    }

    @Override
    public void handleMessage(Serializable message) {

    }

    @Override
    public void handleMessage(Serializable message, String channel) {

    }
}

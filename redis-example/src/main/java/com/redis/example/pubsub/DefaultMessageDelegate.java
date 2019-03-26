package com.redis.example.pubsub;

import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.Serializable;
import java.util.Map;

public class DefaultMessageDelegate implements MessageDelegate {

//    @Override
//    public void handleMessage(String message) {
//        System.out.println("1="+message);
//    }

    @Override
    public void handleMessage(Map message) {
        System.out.println("2="+message);
    }

    @Override
    public void handleMessage(byte[] message) {
        System.out.println("3="+message);
    }
//
//    @Override
//    public void handleMessage(Serializable message) {
//        System.out.println("4="+message);
//    }
//
    @Override
    public void handleMessage(Serializable message, String channel) {
        System.out.println(channel+" = "+message);
    }
}

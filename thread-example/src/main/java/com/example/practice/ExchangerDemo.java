package com.example.practice;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {

    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        Thread consumer = new Thread(()->{
            try {
                exchanger.exchange("hello, world");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread producer = new Thread(()->{

        });

    }
}

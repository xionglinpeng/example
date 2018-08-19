package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section4;

public class Main {

    public static void main(String[] args) {
        EventStroage eventStroage = new EventStroage();

        Producer producer = new Producer(eventStroage);
        Consumer consumer = new Consumer(eventStroage);

        producer.start();
        consumer.start();
    }
}

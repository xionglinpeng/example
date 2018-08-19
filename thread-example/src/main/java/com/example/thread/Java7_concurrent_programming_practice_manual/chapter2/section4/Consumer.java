package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section4;

public class Consumer extends Thread {

    private EventStroage stroage;

    public Consumer(EventStroage stroage) {
        this.stroage = stroage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.stroage.get();
        }
    }
}

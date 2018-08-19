package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section4;

import java.util.concurrent.CountDownLatch;

public class Videoconference implements Runnable {

    private final CountDownLatch countDownLatch;

    Videoconference(int number) {
        countDownLatch = new CountDownLatch(number);
    }

    public void arrive(String name) {
        System.out.printf("%s has arrived.",name);
        countDownLatch.countDown();
        System.out.printf("Videoconference: Waiting for %d participants.\n",countDownLatch.getCount());
    }

    @Override
    public void run() {
        System.out.printf("Videoconference: Initialization: %d participants.\n",countDownLatch.getCount());
        try {
            countDownLatch.await();
            System.out.printf("Videoconference: All the participants have come.\n");
            System.out.printf("Videoconference: Let~s start...\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {

    private final Lock queueLock = new ReentrantLock(false);

    public void printJob(Object document) {
        queueLock.lock();
        long duration = (long)(Math.random()*10000);
        System.out.printf("%s PrintQueue: printing a Jon during %d seconds\n",Thread.currentThread().getName(),duration/1000);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            queueLock.unlock();
        }
    }


}

package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section5;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {

    private final Lock queueLock = new ReentrantLock();

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

package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section2;

import java.util.Objects;
import java.util.concurrent.Semaphore;

public class PrintQueue {

    private final Semaphore semaphore;

    public PrintQueue() {
        this.semaphore = new Semaphore(1);
    }

    public void printJob(Object document) {
        try {
            this.semaphore.acquire();
            long duration = (long)(Math.random()*10);
            System.out.printf("%s: PrintQueue : Printing a Jon during %d seconds.\n",Thread.currentThread().getName(),duration);
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.semaphore.release();
        }
    }
}

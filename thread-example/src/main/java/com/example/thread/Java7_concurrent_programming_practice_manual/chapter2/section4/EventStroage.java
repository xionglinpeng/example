package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section4;

import java.util.Date;
import java.util.LinkedList;

public class EventStroage {

    private int maxSize;
    private LinkedList<Date> storage;

    public EventStroage() {
        this.maxSize = 0b1010;
        this.storage = new LinkedList<>();
    }

    public synchronized void set() {
        while (this.storage.size() == this.maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.storage.add(new Date());
        System.out.printf("Set: %d\n",this.storage.size());
        notifyAll();
    }

    public synchronized void get() {
        while (this.storage.size() == 0B0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Get: %d: %s",this.storage.size(),this.storage.poll());
        notifyAll();
    }
}

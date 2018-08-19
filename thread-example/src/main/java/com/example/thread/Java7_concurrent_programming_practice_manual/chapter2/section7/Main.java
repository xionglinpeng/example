package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section7;

import com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section7.Job;
import com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section7.PrintQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[0B1010];
        for (int i = 0; i < 0B1010; i++) {
            threads[i] = new Thread(new Job(printQueue),"线程->"+i);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
            Thread.sleep(100);
        }
    }
}

package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section5;

public class Main {

    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[0B1010];
        for (int i = 0; i < 0B1010; i++) {
            threads[i] = new Thread(new Job(printQueue),"线程->"+i);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
    }
}

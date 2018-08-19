package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1;

import java.util.concurrent.TimeUnit;

public class Core {

    public static void main(String[] args) throws InterruptedException {
        UnsafeTask task = new UnsafeTask();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(task);
            thread.start();
            TimeUnit.SECONDS.sleep(2);
        }

    }
}

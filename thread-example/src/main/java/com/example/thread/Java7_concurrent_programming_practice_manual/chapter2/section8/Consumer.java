package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section8;

import java.util.Random;

public class Consumer implements Runnable{

    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (this.buffer.hasPendingLines()) {
            String line = this.buffer.get();

        }
    }

    private void processLine(String line) {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

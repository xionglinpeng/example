package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileClock implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s\n",new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.printf("The FileClock has been interrupted.\n");
            }
        }
    }

    public static void main(String[] args) {
        FileClock fileClock = new FileClock();
        Thread thread = new Thread(fileClock);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //中断线程，即使线程休眠，也会马上抛出异常
        thread.interrupt();
    }
}

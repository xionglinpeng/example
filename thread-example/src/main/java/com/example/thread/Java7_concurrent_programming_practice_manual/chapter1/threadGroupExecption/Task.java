package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1.threadGroupExecption;

import java.util.Random;

public class Task implements Runnable {

    @Override
    public void run() {
        int result;
        Random random = new Random(Thread.currentThread().getId());
        while (true){
            result = 1000/((int)random.nextDouble()*1000);
            System.out.printf("%s : %d",Thread.currentThread().getId(),result);
            //isInterrupted()只是单纯的判断当前线程是否中断，并不会重置中断状态
            if (Thread.currentThread().isInterrupted()) {
                System.out.printf("%d : Interrupted\n",Thread.currentThread().getId());
                return;
            }
        }
    }

    public static void main(String[] args) {
        MyThreadGroup threadGroup = new MyThreadGroup("MyThreadGroup");
        Task task = new Task();
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(threadGroup,task);
            thread.start();
        }
    }
}

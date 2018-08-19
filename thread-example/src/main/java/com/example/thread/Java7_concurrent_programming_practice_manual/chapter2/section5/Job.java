package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section5;

public class Job implements Runnable {

    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.printf("%s: 打印文件\n",Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("%s: 文件已经打印出来了。\n",Thread.currentThread().getName());
    }
}

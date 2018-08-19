package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section3;


public class Job implements Runnable {

    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.printf("%s: Going to print a job.\n",Thread.currentThread().getName());
        this.printQueue.printJob(new Object());
        System.out.printf("%s: The document has been printed.\n",Thread.currentThread().getName());
    }
}

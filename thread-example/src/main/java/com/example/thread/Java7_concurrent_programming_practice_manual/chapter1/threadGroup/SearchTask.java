package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1.threadGroup;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SearchTask implements Runnable {

    private Result result;

    public SearchTask(Result result) {
        this.result = result;
    }

    private void doTask() throws InterruptedException {
        Random random = new Random(new Date().getTime());
        int value = (int)(random.nextDouble()*100);
        System.out.printf("Thread %s: %d\n",Thread.currentThread().getName(),value);
        TimeUnit.SECONDS.sleep(value);
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.printf("Thread %s: Start\n",name);
        try {
            this.doTask();
            this.result.setName(name);
        } catch (InterruptedException e) {
            System.out.printf("Thread %s : Interrupted\n",name);
            return;
        }
        System.out.printf("Thread %s: End\n",name);
    }

    private static void waitFinish(ThreadGroup threadGroup) {
        while (threadGroup.activeCount()>9){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("Searcher");
        Result result = new Result();
        SearchTask searchTask = new SearchTask(result);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(threadGroup,searchTask);
            thread.start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Number of Threads: %d\n",threadGroup.activeCount());
        System.out.printf("Infomation about the Thread Group\n");
        threadGroup.list();

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        for (int i = 0; i < threadGroup.activeCount(); i++) {
            System.out.printf("Thread %s: %s\n",threads[i].getName(),threads[i].getState());
        }
        waitFinish(threadGroup);
        threadGroup.interrupt();
    }
}

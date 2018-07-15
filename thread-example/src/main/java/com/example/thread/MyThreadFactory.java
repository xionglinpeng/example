package com.example.thread;

import lombok.Data;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


@Data
public class MyThreadFactory implements ThreadFactory {

    private int counter;
    private String name;
    private List<String> stats;

    MyThreadFactory(String name) {
        this.counter = 0;
        this.name = name;
        stats = new ArrayList<>();
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r,name+"-Thread-"+counter);
        this.counter++;
        this.stats.add(String.format("Created thread %d with name %s on %s\n",thread.getId(),thread.getName(),new Date()));
        return thread;
    }

    public String getStats() {
        StringBuilder buffer = new StringBuilder();
        Iterator<String> iterator = this.stats.iterator();
        while (iterator.hasNext()) {
            buffer.append(iterator.next());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        MyThreadFactory threadFactory = new MyThreadFactory("MyThreadFactory");
        Task task = new Task();
        System.out.println("Start the Threads.");
        for (int i = 0; i < 10; i++) {
            threadFactory.newThread(task).start();
        }
        System.out.println("Factory stats : ");
        System.out.printf("%s\n",threadFactory.getStats());

    }
}

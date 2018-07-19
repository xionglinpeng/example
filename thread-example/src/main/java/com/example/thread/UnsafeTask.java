package com.example.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UnsafeTask implements Runnable {

//    private Date startDate;

    private ThreadLocal<Date> startDate = ThreadLocal.withInitial(Date::new);


    @Override
    public void run() {
//        startDate=new Date();
        System.out.printf("Starting Thread: %s : %s\n",Thread.
                currentThread().getId(),startDate.get());
        try {
            TimeUnit.SECONDS.sleep( (int)Math.rint(Math.random()*0b1010));
        } catch (InterruptedException e) {
            e.getMessage();
        }
        System.out.printf("Thread Finished: %s : %s\n",Thread.
                currentThread().getId(),startDate.get());
    }
}

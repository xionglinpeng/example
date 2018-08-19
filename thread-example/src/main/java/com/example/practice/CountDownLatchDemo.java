package com.example.practice;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class CountDownLatchDemo {


    public static void main(String[] args) {

        CountDownLatch downLatch = new CountDownLatch(3);

        Stream.generate(()->new Thread(()->{
            System.out.printf("子任务：%s\n",Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            downLatch.countDown();
        })).limit(3).forEach(Thread::start);


        try {
            System.out.println("主任务开始执行...");
            System.out.println("等待子任务执行完毕...");
            downLatch.await();
            System.out.println("子任务执行完毕...");
            System.out.printf("开始执行后续主任务...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}

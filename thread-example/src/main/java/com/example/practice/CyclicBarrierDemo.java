package com.example.practice;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java中关于线程的计数器
 */
public class CyclicBarrierDemo {
    public static void err(){
        throw new RuntimeException("2");
    }
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(3,()->{
            System.out.println(Thread.currentThread().getName()+"barrierAction");
        });

        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("barrier.getNumberWaiting()"+barrier.getNumberWaiting());
        System.out.println("barrier.getParties()"+barrier.getParties());

        Thread thread1;
//        for (int i = 0; i < 3; i++) {
//            Thread.sleep(200);

            Thread thread = new Thread(()->{
                try {

                    err();
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            thread1 = new Thread(()->{
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            thread1.start();
            Thread thread2 = new Thread(()->{
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            thread2.start();

//            executor.execute(()->{
//                System.out.println(Thread.currentThread().getName()+"@@@@@@@@@");
//                try {
//
//                    err();
//                    barrier.await();
//                } catch (InterruptedException | BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName()+"**********");
//            });
            System.out.println("barrier.getNumberWaiting()==="+barrier.getNumberWaiting());
            System.out.println("barrier.getParties()==="+barrier.getParties());
//        }
        Thread.sleep(2000);
        //返回当前在屏障处等待的参与者数目。
        System.out.println("barrier.getNumberWaiting()"+barrier.getNumberWaiting());
        //返回要求启动此 barrier 的参与者数目。
        System.out.println("barrier.getParties()"+barrier.getParties());

        executor.shutdown();

        System.out.println(thread.getState());
        System.out.println(thread1.getState());
//        thread.interrupt();
//        thread1.interrupt();
//        barrier.reset();

    }
}

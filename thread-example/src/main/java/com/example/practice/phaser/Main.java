package com.example.practice.phaser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void phases1(Phaser phaser) {
        System.out.printf("阶段1：%s\n",Thread.currentThread().getName());
        //到达,等待推进
//        int phase = phaser.arriveAndAwaitAdvance();
//        System.out.println(phase);

        phaser.awaitAdvance(phaser.arrive());

    }

    public static void phases2(Phaser phaser) {
        System.out.printf("阶段2：%s\n",Thread.currentThread().getName());
//        int phase = phaser.arriveAndAwaitAdvance();
//        System.out.println(phase);
        phaser.awaitAdvance(phaser.arrive());
    }

    public static void phases3(Phaser phaser) {
        System.out.printf("阶段3：%s\n",Thread.currentThread().getName());
//        int phase = phaser.arriveAndAwaitAdvance();
//        System.out.println(phase);
        phaser.awaitAdvance(phaser.arrive());
    }

    public static void main(String[] args) throws Exception {

        //9.   创建新的有3个参与者的 Phaser 对象，名为 phaser。
//        Phaser phaser=new Phaser(3);

        //10. 创建并运行3个线程来执行3个task对象。
//        for (int i=0; i<3; i++) {
//            Task task=new Task(i+1, phaser);
//            Thread thread=new Thread(task);
//            thread.start();
//        }

//11.创建迭代10次的for循环，来学关于phaser对象的信息。
//        for (int i=0; i<10; i++) {
//
////12. 写关于 registered parties 的信息，phaser的phase，到达的parties, 和未到达的parties 的信息。
//            System.out.printf("********************\n");
//            System.out.printf("Main: Phaser Log\n");
//            System.out.printf("Main: Phaser: Phase: %d\n",phaser.getPhase());
//            System.out.printf("Main: Phaser: Registered Parties:%d\n",phaser.getRegisteredParties());
//            System.out.printf("Main: Phaser: Arrived Parties:%d\n",phaser.getArrivedParties());
//            System.out.printf("Main: Phaser: Unarrived Parties:%d\n",phaser.getUnarrivedParties());
//            System.out.printf("********************\n");
//
////13. 让线程休眠1秒，并合上类的循环。
//            TimeUnit.SECONDS.sleep(1);
//        }

        Phaser phaser = new OnPhaser();
        for (int i = 0; i < 3; i++) {
            phaser.register();
        }


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 3; i++) {
            executorService.execute(()->{
                //到达，但是不等待
                int phase = phaser.arrive();
                System.out.println(phase);
                phases1(phaser);
                phases2(phaser);
                phases3(phaser);
                //到达，并且删除注册
                System.out.println(Thread.currentThread().getName()+"~~~~~~~~~~~"+phaser.getRegisteredParties());
                phaser.arriveAndDeregister();
                System.out.println(Thread.currentThread().getName()+"***********"+phaser.getRegisteredParties());
                phases1(phaser);
            });
        }
        executorService.shutdown();
        if (executorService.isShutdown()){
            System.out.println("线程池已经关闭。。。");
        }


    }
}

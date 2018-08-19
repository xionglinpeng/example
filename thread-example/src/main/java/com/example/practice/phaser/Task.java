package com.example.practice.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

    private int time;

    private Phaser phaser;

    public Task(int time, Phaser phaser) {
        this.time = time;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        //到达这个阶段，不要等待别人的到来。
        phaser.arrive();
        //6.写信息到操控台表明阶段一开始，把线程放入休眠几秒，使用time属性来表明，再写信息到操控台表明阶段一结束，
        // 并使用 phaser 属性的 arriveAndAwaitAdvance() 方法来与剩下的任务同步。
        System.out.printf("%s: Entering phase 1.\n",Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s: Finishing phase 1.\n",Thread. currentThread().getName());
        //到达,等待推进
        phaser.arriveAndAwaitAdvance();
        //7.为第二和第三阶段重复第一阶段的行为。在第三阶段的末端使用 arriveAndDeregister()方法代替
        // arriveAndAwaitAdvance() 方法。
        System.out.printf("%s: Entering phase 2.\n",Thread. currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s: Finishing phase 2.\n",Thread. currentThread().getName());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s: Entering phase 3.\n",Thread. currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s: Finishing phase 3.\n",Thread. currentThread().getName());
        //到达这个阶段后，无需等待其他人的到来。
        phaser.arriveAndDeregister();
    }
}

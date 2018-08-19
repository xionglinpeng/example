package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section6;

import java.util.concurrent.Phaser;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser(3);
        FileSearch system = new FileSearch("","log",phaser);
        FileSearch apps = new FileSearch("","log",phaser);
        FileSearch docyments = new FileSearch("","log",phaser);
        Thread systemThread = new Thread(system,"System");
        systemThread.start();
        Thread appsThread = new Thread(system,"Apps");
        appsThread.start();
        Thread documentsThread = new Thread(system,"Documents");
        documentsThread.start();


        systemThread.join();
        appsThread.join();
        documentsThread.join();
        System.out.printf("Terminated: %s",phaser.isTerminated());
    }
}

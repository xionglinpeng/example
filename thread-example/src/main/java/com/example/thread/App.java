package com.example.thread;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("thread1");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("thread2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        System.out.println(11);
        thread2.join();
        System.out.println("main:" + (System.currentTimeMillis() - startTime));
    }
}

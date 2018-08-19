package com.example;

public class Validate {

    private static boolean isStop = true;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            while (isStop){

            }
            System.out.println("退出了");
        });
        thread.start();
        isStop = false;
        thread.join();
    }
}

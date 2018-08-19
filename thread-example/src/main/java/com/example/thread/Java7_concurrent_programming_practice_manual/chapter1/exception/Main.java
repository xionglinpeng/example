package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1.exception;

public class Main {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        Task task = new Task();
        Thread thread = new Thread(task);
//        thread.setUncaughtExceptionHandler(new ExceptionHandler());
        thread.start();
    }
}

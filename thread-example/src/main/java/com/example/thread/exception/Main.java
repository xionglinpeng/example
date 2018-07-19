package com.example.thread.exception;

public class Main {

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        Task task = new Task();
        Thread thread = new Thread(task);
//        thread.setUncaughtExceptionHandler(new ExceptionHandler());
        thread.start();
    }
}

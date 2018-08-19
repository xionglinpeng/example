package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1.daemon;

import java.util.ArrayDeque;
import java.util.Deque;

public class Main {

    public static void main(String[] args) {
        Deque<Event> deque = new ArrayDeque<>();
        WriterTask writerTask = new WriterTask(deque);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(writerTask);
            thread.start();
        }
        CleanerTask cleanerTask = new CleanerTask(deque);
        cleanerTask.start();
    }
}

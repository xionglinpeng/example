package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section8;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Main {

    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();
        Exchanger<List<String>> exchanger = new Exchanger<>();

        Producer producer = new Producer(buffer1,exchanger);
        Comsumer consumer = new Comsumer(buffer2,exchanger);

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

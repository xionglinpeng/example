package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section2;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class Account {

    private double balance;

    public synchronized void addAmount(double amount){
        double tmp = balance;
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tmp+=amount;
        balance = tmp;
    }

    public synchronized void subtractAmount(double amount) {
        double tmp = balance;
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tmp-=amount;
        balance = tmp;
    }
}



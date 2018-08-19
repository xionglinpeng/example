package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section2;

public class Company implements Runnable {

    private Account account;

    public Company(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            account.addAmount(1000);
        }
    }
}

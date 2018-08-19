package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section2;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Account account = new Account();
        account.setBalance(1000);

        Company company = new Company(account);
        Thread companyThread = new Thread(company);

        Bank bank = new Bank(account);
        Thread bankThread = new Thread(bank);

        System.out.printf("Account : initial Balance : %f\n",account.getBalance());
        companyThread.start();
        bankThread.start();

        companyThread.join();
        bankThread.join();
    }
}

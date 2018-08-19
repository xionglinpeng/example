package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section6;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PricesInfo {

    private double price1;
    private double price2;

    private ReadWriteLock lock;

    public PricesInfo() {
        this.price1 = 1.0;
        this.price2 = 1.0;
        lock = new ReentrantReadWriteLock();
    }

    public double getPrice1() {
        lock.readLock().lock();
        double value = price1;
        lock.readLock().unlock();
        return value;
    }

    public double getPrice2() {
        lock.readLock().lock();
        double value = price2;
        System.out.println("2222222");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.readLock().unlock();
        return value;
    }

    public void setPrices(double price1,double price2) {
        lock.writeLock().lock();
        this.price1 = price1;
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        this.price2 = price2;
        lock.writeLock().unlock();
    }
}

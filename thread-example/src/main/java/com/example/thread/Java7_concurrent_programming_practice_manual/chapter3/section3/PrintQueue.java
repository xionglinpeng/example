package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section3;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {

    private final Semaphore semaphore;

    private boolean freePrintters[];

    private Lock lockPrinters;

    public PrintQueue() {
        this.semaphore = new Semaphore(3);
        freePrintters = new boolean[3];
        for (int i = 0; i < 3; i++) {
            freePrintters[i] = true;
        }
        lockPrinters = new ReentrantLock();
    }

    public void printJob(Object document) {
        try {
            this.semaphore.acquire();
            int assignedPrinter = getPrinter();
            long duration = (long)(Math.random()*10);
            System.out.printf("%s: PrintQueue : Printing a Jon during %d seconds.\n",Thread.currentThread().getName(),duration);
            Thread.sleep(duration);
            freePrintters[assignedPrinter] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.semaphore.release();
        }
    }

    private int getPrinter() {
        int ret = -1;
        lockPrinters.lock();

        for (int i = 0; i < freePrintters.length; i++) {
            if (freePrintters[i]) {
                ret = i;
                freePrintters[i] = false;
                break;
            }
        }

        lockPrinters.unlock();

        return ret;
    }
}

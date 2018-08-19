package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section6;

public class Writer implements Runnable {

    private PricesInfo pricesInfo;

    public Writer(PricesInfo pricesInfo) {
        this.pricesInfo = pricesInfo;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.printf("Writer: 试图修改价格。\n");
            pricesInfo.setPrices(Math.random()*10,Math.random()*8);
            System.out.printf("Writer: 价格已经修改。\n");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

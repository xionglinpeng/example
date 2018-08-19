package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ThreadJoin {

    static class DataSourcesLoader implements Runnable {
        @Override
        public void run() {
            System.out.printf("Begining data sources loading: %s\n",new Date());
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Data sources loading has finish %s\n",new Date());
        }
    }

    static class NetworkConnectionsLoader implements Runnable {
        @Override
        public void run() {
            System.out.printf("Begining netwrok connections : %s\n",new Date());
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Data netwrok connections has finish %s\n",new Date());
        }
    }

    public static void main(String[] args) {
        Thread dataSourcesLoader = new Thread(new DataSourcesLoader());
        Thread networkConnectionsLoader = new Thread(new NetworkConnectionsLoader());
        dataSourcesLoader.start();
        networkConnectionsLoader.start();
        try {
            dataSourcesLoader.join();
            networkConnectionsLoader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main : Configuration has been loaded : %s\n",new Date());
    }
}

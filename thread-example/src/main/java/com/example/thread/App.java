package com.example.thread;


import org.junit.Test;

import javax.swing.table.TableRowSorter;

/**
 * Hello world!
 */
public class App {

    private final Object $lock = new Object();

    @Test
    public void waitAndNotify() throws Exception{
        Thread thread = new Thread(()->{
            String thredName = Thread.currentThread().getName();
            try {
                System.out.println(thredName + " before sleep.");
                Thread.sleep(3000);
                System.out.println(thredName + " after sleep.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thredName + " before notity lock.");
            synchronized ($lock) {
                $lock.notifyAll();
            }
            System.out.println(thredName + " after notity lock.");
        });
        thread.start();

        System.out.println("main : get lock.");
        synchronized ($lock) {
            System.out.println("main : release lock.");
            //一旦调用锁的wait()方法，将会释放当前锁。
            $lock.wait();
        }
        System.out.println("main : finish.");


    }




    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("thread1");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("thread2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        System.out.println(11);
        thread2.join();
        System.out.println("main:" + (System.currentTimeMillis() - startTime));
    }
}

package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section2;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

public class Demo {


    private Semaphore semaphore = new Semaphore(1);//信号

    public void vist() {
        try {
            //获得锁；acquire->获得
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName()+"进去了。");
            Thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //是否锁；release->释放
            semaphore.release();
            System.out.println(Thread.currentThread().getName()+"释放了。");
        }

    }




    public static void main(String[] args) {
        final Demo demo = new Demo();
        Stream.generate(()->new Thread(demo::vist)).limit(2).forEach(Thread::start);
    }
}

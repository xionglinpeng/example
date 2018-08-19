package com.example.practice.threadPool;


import java.util.Random;
import java.util.concurrent.*;


class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        System.out.println("执行任务之前调用");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        System.out.println("执行任务之后调用");
    }

    @Override
    protected void terminated() {
        System.out.println("线程池关闭时调用");
    }
}


public class ThreadPoolExecutorDemo {






    public static void main(String[] args) throws InterruptedException {
        /*
        int corePoolSize : 池中所保存的线程数，包括空闲线程。
        int maximumPoolSize :池中允许的最大线程数。
        long keepAliveTime : 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
        TimeUnit unit :  keepAliveTime 参数的时间单位。
        BlockingQueue<Runnable> workQueue : 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
        ThreadFactory threadFactory : 执行程序创建新线程时使用的工厂。
        RejectedExecutionHandler handler : 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序。
        */


        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,10,30, TimeUnit.SECONDS,workQueue);
        for (int i = 0; i < 11; i++) {
            executor.execute(()->{
                System.out.printf("[%s] : Hello.\n",Thread.currentThread().getName());
            });
        }

        executor.shutdown();

    }
}

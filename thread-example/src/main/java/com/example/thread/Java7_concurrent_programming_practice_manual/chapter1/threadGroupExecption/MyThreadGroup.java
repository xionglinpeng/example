package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1.threadGroupExecption;

public class MyThreadGroup extends ThreadGroup{


    public MyThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
//        super.uncaughtException(t, e);
        System.out.printf("The thread %s has thrown an Exception\n",t.getId());
        e.printStackTrace(System.out);
        System.out.printf("Terminating the rest of Threads\n");
        interrupt();
    }
}

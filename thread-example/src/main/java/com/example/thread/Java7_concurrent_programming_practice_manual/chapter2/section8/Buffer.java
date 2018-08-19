package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section8;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {

    /*用来存放共享属性*/
    private LinkedList<String> buffer;

    /*用来存放buffer的长度*/
    private int maxSize;

    /*用来对修改buffer的代码块进行控制*/
    private ReentrantLock lock;

    private Condition lines;

    private Condition space;

    /*用来表明缓冲区是否还有数据*/
    private boolean pendingLines;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        this.buffer = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.lines = this.lock.newCondition();
        this.space = this.lock.newCondition();
        this.pendingLines = true;
    }

    public void insert(String line) {
        this.lock.lock();
        try {
            while (this.buffer.size() == maxSize) {
                this.space.await();
            }
            this.buffer.offer(line);
            System.out.printf("%s: Insert Line: %d\n",Thread.currentThread().getName(),this.buffer.size());
            this.lines.signalAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.lock.unlock();
        }
    }

    public String get() {
        String line = null;
        this.lock.lock();
        try {
            while (this.buffer.size() == 0 && this.hasPendingLines()){
                this.lines.await();
            }
            if (this.hasPendingLines()) {
                line = this.buffer.poll();
                System.out.printf("%s: Line Readed: %d\n",Thread.currentThread().getName(),this.buffer.size());
                this.space.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }
        return line;
    }

    public void setPendingLines(boolean pendingLines) {
        this.pendingLines = pendingLines;
    }

    /**
     * 如果有数据行可以处理的时候返回true，否则返回false。
     * @return
     */
    public boolean hasPendingLines() {
        return this.pendingLines || this.buffer.size() > 0;
    }
}

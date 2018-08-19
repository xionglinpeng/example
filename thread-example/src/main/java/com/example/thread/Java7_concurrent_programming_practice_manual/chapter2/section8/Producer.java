package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section8;

public class Producer implements Runnable{

    private FileMock mock;

    private Buffer buffer;

    public Producer(FileMock mock, Buffer buffer) {
        this.mock = mock;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        this.buffer.setPendingLines(true);
        while (this.mock.hasMoreLines()) {
            String line = this.mock.getLine();
            this.buffer.insert(line);
        }
        this.buffer.setPendingLines(false);
    }
}

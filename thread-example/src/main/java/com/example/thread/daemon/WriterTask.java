package com.example.thread.daemon;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class WriterTask implements Runnable {

    private Deque<Event> deque;

    public WriterTask(Deque<Event> deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        Event event = new Event();
        for (int i = 0; i < 100; i++) {
            Event e = event.clone();
            e.setDate(new Date());
            e.setEvent(String.format("The thread %s has generated an event",Thread.currentThread().getId()));
            deque.addFirst(e);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}

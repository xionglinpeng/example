package com.example.thread.daemon;

import java.util.Date;
import java.util.Deque;
import java.util.function.Supplier;

public class CleanerTask extends Thread {

    private Deque<Event> deque;

    public CleanerTask(Deque<Event> deque) {
        this.deque = deque;
        //设置当前线程为守护线程
        super.setDaemon(Boolean.TRUE);
    }

    @Override
    public void run() {
        while (true) {
            this.clean(Date::new);
        }
    }

    public void clean(Supplier<Date> date) {
        long difference;
        boolean delete;
        if (deque.isEmpty()) {
            return;
        }
        delete = false;

        do {
            Event e = deque.getLast();
            difference = date.get().getTime() - e.getDate().getTime();
            if (difference > 10000) {
                System.out.printf("Cleaner: %s\n",e.getEvent());
                deque.removeLast();
                delete = true;
            }
        } while (difference > 10000);

        if (delete) {
            System.out.printf("Cleaner: Size of the queue: %d\n",deque.size());
        }
    }
}

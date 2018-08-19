package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section3;

public class TickedOffice2 implements Runnable{

    private Cinema cinema;

    public TickedOffice2(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        cinema.sellTicksts2(2);
        cinema.sellTicksts2(4);
        cinema.sellTickstsl(2);
        cinema.sellTickstsl(1);
        cinema.returnTickets2(2);
        cinema.sellTickstsl(3);
        cinema.sellTicksts2(2);
        cinema.sellTickstsl(2);
    }
}

package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section3;

public class TickedOffice1 implements Runnable{

    private Cinema cinema;

    public TickedOffice1(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        cinema.sellTickstsl(3);
        cinema.sellTickstsl(2);
        cinema.sellTicksts2(2);
        cinema.returnTickets1(3);
        cinema.sellTickstsl(5);
        cinema.sellTicksts2(2);
        cinema.sellTicksts2(2);
        cinema.sellTicksts2(2);
    }
}

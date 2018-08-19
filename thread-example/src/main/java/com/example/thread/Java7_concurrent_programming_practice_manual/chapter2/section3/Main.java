package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section3;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Cinema cinema = new Cinema();

        TickedOffice1 tickedOffice1 = new TickedOffice1(cinema);
        Thread thread1 = new Thread(tickedOffice1);

        TickedOffice1 tickedOffice2 = new TickedOffice1(cinema);
        Thread thread2 = new Thread(tickedOffice2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.printf("Room 1 Vacancied : %d\n",cinema.getVacanciesCinema1());
        System.out.printf("Room 2 Vacancied : %d\n",cinema.getVacanciesCinema2());
    }
}

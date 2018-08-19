package com.example.thread.Java7_concurrent_programming_practice_manual.chapter2.section3;

import lombok.Data;

@Data
public class Cinema {

    private long vacanciesCinema1;
    private long vacanciesCinema2;

    private final Object controlCinema1,controlCinema2;

    public Cinema() {
        this.controlCinema1 = new Object();
        this.controlCinema2 = new Object();
        this.vacanciesCinema1 = 20;
        this.vacanciesCinema2 = 20;
    }

    public boolean sellTickstsl(int number) {
        synchronized (controlCinema1) {
            if (number < vacanciesCinema1) {
                vacanciesCinema1 -= number;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean sellTicksts2(int number) {
        synchronized (controlCinema2) {
            if (number < vacanciesCinema2) {
                vacanciesCinema2 -= number;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean returnTickets1(int number) {
        synchronized (controlCinema1) {
            vacanciesCinema1 += number;
            return true;
        }
    }

    public boolean returnTickets2(int number) {
        synchronized (controlCinema2) {
            vacanciesCinema2 += number;
            return true;
        }
    }
}

package com.example.structure.proxy.chapt0;

public class WorkImpl implements IWork {


    @Override
    public void startWork() {
        System.out.println("I'm firefighter, I start work.");
    }

    @Override
    public String obtainWork() {
        return "I'm artist.";
    }

    @Override
    public void saveWork(String work) {
        System.out.println("save work : engineer.");
    }


}

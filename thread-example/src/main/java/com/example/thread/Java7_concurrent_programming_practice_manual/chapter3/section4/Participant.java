package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section4;

import java.util.concurrent.TimeUnit;

public class Participant implements Runnable {

    private Videoconference videoconference;

    private String name;

    public Participant(Videoconference videoconference, String name) {
        this.videoconference = videoconference;
        this.name = name;
    }

    @Override
    public void run() {
        long duration = (long)(Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        videoconference.arrive(name);
    }
}

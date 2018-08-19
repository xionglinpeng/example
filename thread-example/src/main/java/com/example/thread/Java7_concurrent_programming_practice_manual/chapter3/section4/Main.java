package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section4;

public class Main {
    public static void main(String[] args) {
        Videoconference videoconference = new Videoconference(10);

        Thread thread = new Thread(videoconference);
        thread.start();

        for (int i = 0; i < 10; i++) {
            Participant p = new Participant(videoconference,"Participant-"+i);
            new Thread(p).start();
        }
    }
}

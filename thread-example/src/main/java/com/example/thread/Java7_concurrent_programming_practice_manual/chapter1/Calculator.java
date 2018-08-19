package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Calculator implements Runnable {

    private int number;

    private static Thread[] threads = new Thread[10];
    private static Thread.State[] states = new Thread.State[10];

    public Calculator(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.printf("%s : %d * %d = %d\n",Thread.currentThread().getName(),number,i,number*i);
        }
    }

    public static void writeThreadInfo(PrintWriter writer,Thread thread,Thread.State state) {
        writer.printf("Main : Id %d - %s\n",thread.getId(),thread.getName());
        writer.printf("Main : Priority: %d\n",thread.getPriority());
        writer.printf("Main : Old State : %s\n",state);
        writer.printf("Main : New State : %s\n",thread.getState());
        writer.printf("Main : *************************************************\n");
    }


    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            Calculator calculator = new Calculator(i);
            threads[i] = new Thread(calculator);
            if (i%2 == 0) {
                threads[i].setPriority(Thread.MAX_PRIORITY);
            } else {
                threads[i].setPriority(Thread.MIN_PRIORITY);
            }
            threads[i].setName("Thread "+ i);
        }

        try (FileWriter fileWriter = new FileWriter("C:\\Users\\xlp\\Desktop\\log.txt")) {
            try (PrintWriter printWriter = new PrintWriter(fileWriter)) {

                for (int i = 0; i < 10; i++) {
                    printWriter.println("Main : Status Of Thread"+i+" : "+threads[i].getState());
                    states[i] = threads[i].getState();
                }

                for (int i = 0; i < 10; i++) {
                    threads[i].start();
                }

                boolean finish = false;
                while (!finish) {
                    for (int i = 0; i < 10; i++) {
                        if (threads[i].getState() != states[i]) {
                            writeThreadInfo(printWriter,threads[i],states[i]);
                            states[i] = threads[i].getState();
                        }
                    }
                    finish = true;
                    for (int i = 0; i < 10; i++) {
                        finish = finish && (threads[i].getState() == Thread.State.TERMINATED);
                    }
                }
            }
        }
    }
}

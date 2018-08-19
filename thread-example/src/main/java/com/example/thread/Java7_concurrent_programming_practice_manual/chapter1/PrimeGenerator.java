package com.example.thread.Java7_concurrent_programming_practice_manual.chapter1;

public class PrimeGenerator extends Thread {


    private boolean isPrime(long number) {
        if (number <= 2) return true;
        for (int i = 2; i < number; i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    @Override
    public void run() {
        long number = 1L;
        while (true) {
            if(this.isPrime(number)) {
                System.out.printf("Number %d is prime.\n",number);
            }
            if (super.isInterrupted()) {
                System.out.printf("The Prime Genertor has been Interrupted\n");
                break;
            }
            number++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        primeGenerator.start();
        Thread.sleep(5000);
        primeGenerator.interrupt();

    }
}

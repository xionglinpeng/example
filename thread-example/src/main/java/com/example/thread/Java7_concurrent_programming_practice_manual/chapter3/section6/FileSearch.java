package com.example.thread.Java7_concurrent_programming_practice_manual.chapter3.section6;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class FileSearch implements Runnable {

    private String initPath;
    private String end;
    private ArrayList<String> results;
    /*控制任务不同阶的同步*/
    private Phaser phaser;

    public FileSearch(String initPath, String end, Phaser phaser) {
        this.initPath = initPath;
        this.end = end;
        this.phaser = phaser;
        this.results = new ArrayList<>();
    }

    private void directoryPrecess(File file) {
        File[] files = file.listFiles();
        if (file != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()){
                    directoryPrecess(files[i]);
                } else {
                    filePrecess(files[i]);
                }
            }
        }
    }

    private void filePrecess(File file) {
        if (file.getName().endsWith(end)) {
            results.add(file.getAbsolutePath());
        }
    }

    private void fileterResults() {
        ArrayList<String> newResults = new ArrayList<>();
        long actualDate = new Date().getTime();
        for (int i = 0; i < results.size(); i++) {
            File file = new File(results.get(i));
            long fileDate = file.lastModified();
            if (actualDate - fileDate < TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS));
            newResults.add(results.get(i));
        }
        results = newResults;
    }

    private boolean checkResults() {
        if (results.isEmpty()) {
            System.out.printf("%s: Phase %d: 0 results.\n",Thread.currentThread().getName(),phaser.getPhase());
            System.out.printf("%s: Phase %d: End.\n",Thread.currentThread().getName(),phaser.getPhase());
            phaser.arriveAndDeregister();
            return false;
        } else {
            System.out.printf("%s: Phare %d: %d results.\n",Thread.currentThread().getName(),phaser.getPhase(),results.size());
            phaser.arriveAndAwaitAdvance();
            return false;
        }
    }
    
    private void showInfo() {
        for (int i = 0; i < results.size(); i++) {
            File file = new File(results.get(i));
            System.out.printf("%s: %s\n",Thread.currentThread().getName(),file.getAbsolutePath());
        }
        phaser.arriveAndAwaitAdvance();
    }

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Satrting.\n",Thread.currentThread().getName());
        File file = new File(initPath);
        if (file.isDirectory()) {
            directoryPrecess(file);
        }
        if (!checkResults()) {
            return;
        }
        fileterResults();
        if (!checkResults()) {
            return;
        }
        showInfo();
        phaser.arriveAndDeregister();
        System.out.printf("%s: Work completed.\n",Thread.currentThread().getName());
    }

}

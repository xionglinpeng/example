package com.example.thread;

import lombok.AllArgsConstructor;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class FileSearch implements Runnable {

    private String initPath;
    private String fileName;


    private void directoryProcess(File file) throws InterruptedException {
        File[] files = file.listFiles();
        if (Objects.nonNull(files)) {
            for (File f : files) {
                if (f.isDirectory()) {
                    this.directoryProcess(f);
                } else {
                    this.fileProcess(f);
                }

            }
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    private void fileProcess(File file) throws InterruptedException {
        if (file.getName().equals(this.fileName)) {
            System.out.printf("%s : %s\n",Thread.currentThread().getName(),file.getAbsoluteFile());
        }
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }


    @Override
    public void run() {
        File file = new File(this.initPath);
        if (file.isDirectory()) {
            try {
                this.directoryProcess(file);
            } catch (InterruptedException e) {
                System.out.printf("%s : The search has been interrupted",Thread.currentThread().getName());
            }
        }
    }

    public static void main(String[] args) {
        FileSearch fileSearch = new FileSearch("D:\\program","LunarBaseVrApplication.java");
        Thread thread = new Thread(fileSearch);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}

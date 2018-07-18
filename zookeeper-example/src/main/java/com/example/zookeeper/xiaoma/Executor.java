package com.example.zookeeper.xiaoma;

import lombok.Synchronized;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.Objects;

public class Executor implements Watcher,Runnable,DataMonitorListener {

    String znode;

    DataMonitor dm;

    ZooKeeper zk;

    String fileName;

    String exec[];

    Process process;

    public Executor(String hostPort,String znode,String fileName,String exec[]) throws IOException {
        this.fileName = fileName;
        this.exec = exec;
        this.znode = znode;
        this.zk = new ZooKeeper(hostPort,3000,this);
        this.dm = new DataMonitor(zk,znode,null,this);
    }

    static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        StreamWriter(OutputStream os,InputStream is) {
            this.os = os;
            this.is = is;
        }

        @Override
        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b,0,rc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void exists(byte[] data) {
        if (Objects.isNull(data)) {
            //子进程如果存在，则销毁子进程，并阻塞等待
            if (Objects.nonNull(this.process)) {
                System.out.println("Killing process.");
                this.process.destroy();
                try {
                    this.process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (Objects.nonNull(this.process)) {
                System.out.println("Stopping process.");
                this.process.destroy();
                try {
                    this.process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try(FileOutputStream fos = new FileOutputStream(this.fileName)){
                fos.write(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("Starting process.");
                process = Runtime.getRuntime().exec(exec);
                new StreamWriter(System.out, this.process.getInputStream());
                new StreamWriter(System.err, this.process.getErrorStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this){
            notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            synchronized (this){
                while (!this.dm.dead){
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        this.dm.process(watchedEvent);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 0b100) {
            System.err.println("USAGE: Executor hostPort znode filename program [args ...]");
            System.exit(1<<1);
        }
        String hostPort = args[0b0];
        String znode = args[0b1];
        String fileName = args[0b10];
        String exec[] = new String[args.length - 0b11];
        System.arraycopy(args,0b11,exec,0b0,exec.length);
        new Executor(hostPort,znode,fileName,exec).run();
    }
}

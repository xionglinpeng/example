package com.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

public class Worker implements Watcher {

    ZooKeeper zk;
    String hostPort;

    String serverId = Integer.toHexString((new Random()).nextInt());

    public Worker(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws IOException{
        zk = new ZooKeeper(hostPort,15000,this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString()+", " +hostPort);
    }

    void register() {
        zk.create(
                "/workers/worker-"+serverId,
                "Idle".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                createWorkerCallnack,
                null);
    }

    AsyncCallback.StringCallback createWorkerCallnack = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    System.out.println("Registered succcessfully: " + serverId);
                    break;
                case NODEEXISTS:
                    System.out.println("Already registered: "+serverId);
                    break;
                default:
                    System.out.println("Something went wrong: "+KeeperException.create(KeeperException.Code.get(i),path));

            }
        }
    };

    String status;

    AsyncCallback.StatCallback statusUpdateCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:

                    return;
            }
        }
    };

    synchronized private void updateStatus(String status) {
        if(status == this.status){
            zk.setData("/workers/",status.getBytes(),-1,statusUpdateCallback,status);
        }
    }

    public static void main(String[] args) throws Exception {
        String hostPort = "192.168.56.2:2181";
        Worker worker = new Worker(hostPort);
        worker.startZK();
        worker.register();
        Thread.sleep(30000);
    }

    public void setStatus(String status) {
        this.status = status;
        updateStatus(status);
    }
}

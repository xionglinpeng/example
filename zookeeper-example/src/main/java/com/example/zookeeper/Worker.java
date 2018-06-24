package com.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
                "/ /worker-"+serverId,
                "Idle".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                createWorkerCallback,
                null);
    }

    AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
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

    Watcher workersChangeWatcher= e -> {
       if (e.getType() == Event.EventType.NodeChildrenChanged) {
           assert "/workers".equals(e.getPath());
            getWorkers();
       }
    };

    void getWorkers() {
        zk.getChildren("/workers",workersChangeWatcher,workersGetChildrenCallback,null);
    }

    AsyncCallback.ChildrenCallback workersGetChildrenCallback = (int i, String s, Object o, List<String> list)-> {
        switch (KeeperException.Code.get(i)) {
            case CONNECTIONLOSS:
                getWorkers();
                break;
            case OK:
                reassignAndSet(list);
                break;
            default:
                System.out.println("getChildren failed"+KeeperException.create(KeeperException.Code.get(i))+s);
        }
    };

    private ChildrenCache workersCache;

    void reassignAndSet(List<String> children) {
        List<String> toPrecess;
        if (workersCache == null) {
            workersCache = new ChildrenCache(children);
            toPrecess = null;
        } else {
            System.out.println("Removeing and setting");
            toPrecess = workersCache.removeAndSet(children);
        }
        if (toPrecess != null) {
            toPrecess.forEach(this::getAbsentWorkerTasks);
        }

    }

    void getAbsentWorkerTasks(String worker) {

    }

    synchronized private void updateStatus(String status) {
        if(status == this.status){
            zk.setData("/workers/",status.getBytes(),-1,statusUpdateCallback,status);
        }
    }

    Watcher nnewTaskWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                assert new String("/assign/worker-"+serverId).equals(watchedEvent.getPath());
                getTasks();
            }
        }
    };

    void getTasks() {
        zk.getChildren("/assign/worker-"+serverId,nnewTaskWatcher,taskGetChildrenCallback,null);
    }

    ThreadPoolExecutor executor = new ThreadPoolExecutor(1,1,1, TimeUnit.DAYS,new LinkedBlockingDeque<>(100));
    List<String> onGingTasks = new ArrayList<>();

    AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {

        }
    };

    AsyncCallback.ChildrenCallback taskGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int i, String s, Object o, List<String> children) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    if (children != null) {
                        executor.execute(new Runnable() {
                            List<String> children;
                            DataCallback cb;

                            public Runnable init(List<String> children,DataCallback cb) {
                                this.children = children;
                                this.cb = cb;
                                return this;
                            }

                            @Override
                            public void run() {
                                System.out.println("Looping into tasks.");
                                synchronized (onGingTasks) {
                                    for (String task : children) {
                                        if(!onGingTasks.contains(task)){
                                            System.out.println("New task: "+task);
                                            zk.getData("/assign/worker-"+serverId+"/"+task,
                                                    false,cb,task);
                                            onGingTasks.add(task);
                                        }
                                    }
                                }

                            }
                        }.init(children,taskDataCallback));
                    }
                    default:
                        System.out.println("getChildren faild: "+KeeperException.create(KeeperException.Code.get(i))+s);
            }
        }
    };


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

package com.example.zookeeper.funtion;

import com.example.zookeeper.ZKConst;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

public class SynchQueue implements Watcher {

    static final int DEFAULT_INITIAL_CAPACITY = 0B11;//0B10000;//aka 16

    static final int DEFAULT_ZK_SESSION_TIMEOUT = 0B11101010011000; //aka 15000

    int initCapacity = 0;

    String root;

    String name;

    ZooKeeper zk;

    SynchQueue(String root, String connectString, int sessionTimeout, int initialCapacity) {
        this.initCapacity = initialCapacity;
        this.root = root;
        try {
            this.name = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            zk = new ZooKeeper(connectString,sessionTimeout,this);
            if (Objects.isNull(zk.exists(root,false))) {
                zk.create(root,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    SynchQueue(String root, String connectString, int sessionTimeout) {
        this(root,connectString,sessionTimeout,DEFAULT_INITIAL_CAPACITY);
    }

    SynchQueue(String root, String connectString) {
        this(root,connectString,DEFAULT_ZK_SESSION_TIMEOUT,DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeCreated &&
                watchedEvent.getPath().equals(root+"/start")) {
            System.out.println("The queue element is full.");
            synchronized (this){
                this.notifyAll();
            }
        }
    }

    public void watcherQueueFull() {
        zk.exists(root+"/start",true,(rc,path,ctx,name)->{
            KeeperException.Code code = KeeperException.Code.get(rc);
            switch (code) {
                case CONNECTIONLOSS:
                    this.watcherQueueFull();
                case OK:
                    System.out.println("watcher queue full successfull.");
                default:
                    System.out.printf("watcher queue full error : %s\n",code);
            }
        },null);
    }

    public void addQueue() {
        this.watcherQueueFull();//监视队列是否满了
        try {
            zk.create(root+"/"+name,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            synchronized (this) {
                //获取队列中的元素数量
                List<String> childrens = zk.getChildren(root,false);
                //如果队列中的元素数量没有满
                if (childrens.size() < initCapacity) {
                    //阻塞当前进程，即当前进程不能再添加了
                    this.wait();
                } else {
                    zk.create(root+"/start",new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
                }
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SynchQueue queue = new SynchQueue("/SYNCH-QUEUE",ZKConst.CONNECT_STRING);
        queue.addQueue();
    }
}

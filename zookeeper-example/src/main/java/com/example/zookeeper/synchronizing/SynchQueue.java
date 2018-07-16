package com.example.zookeeper.synchronizing;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SynchQueue implements Watcher {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;//aka 16

    static final int DEFAULT_ZK_SESSION_TIMEOUT = 15000;

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
                zk.create("/synchronizing",new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
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
            System.out.println("队列元素已经满了。");
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
        this.watcherQueueFull();
        try {


            zk.create(root/+"/"+name,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            //获取队列中的元素数量
            List<String> childrens = zk.getChildren(root,false);
            //如果队列中的元素数量没有满
            if (childrens.size() < initCapacity) {

            } else {
                zk.create(root+"/start",new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package com.example.zookeeper.locks;

import com.example.zookeeper.ZookeeperApplication;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Locks implements Watcher {

    private final Object $lock = new Object[0];

    private String connectString = "127.0.0.1:2181";

    private int sessionTimeout = 15000;

    private ZooKeeper zk;

    private String myZNode;

    private static final String ROOT = "/locks";


    public Locks() throws Exception{
//        zk = new ZooKeeper(this.connectString,this.sessionTimeout,this);
//        if (zk.exists(ROOT,false) == null) {
//            zk.create(ROOT,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        }
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType() == Event.EventType.NodeDeleted){
            this.notifyAllWait();
        }
    }

    @Synchronized
    public void notifyAllWait() {
        $lock.notifyAll();
    }


    public void getLock() throws Exception{
        List<String> list = this.getZk().getChildren(ROOT,false);
//        String[] childrens = list.toArray(new String[list.size()]);
        Arrays.sort(list.toArray());
        String leastNode = list.get(0);
        if(this.getMyZNode().equals(leastNode)){
            this.doAction();
        } else {
            this.waitForLock(leastNode);
        }
    }

    public void waitForLock(@NonNull String leastNode) throws Exception {
        Stat stat = this.getZk().exists(leastNode,true);
        if (stat != null) {
            $lock.wait();
        }
        this.getLock();
    }


    public void doAction() {
        System.out.println(".........................");
    }


    public void start() throws Exception {
        this.setMyZNode(this.getZk().create(ROOT+"/lock-",new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL));
        this.getLock();
    }




    public static void main(String[] args) throws Exception{
//        Locks locks = new Locks();
//        locks.start();
//        List<Locks> locks = new ArrayList<>();
//        locks.add(new Locks());locks.add(new Locks());
//        List<Integer> assignUsers2 = locks.stream().collect(ArrayList::new,(a, b)->{
////            a.add(b.getSessionTimeout());
//            System.out.println("a="+a);
//            System.out.println("b="+b);
//        },null);
//        System.out.println("aaa"+assignUsers2);
//
//        Files.readAllLines(Paths.get(""));

//        StandardCharsets.UTF_8

        System.out.println(Paths.get("Locks").toFile());
    }
}

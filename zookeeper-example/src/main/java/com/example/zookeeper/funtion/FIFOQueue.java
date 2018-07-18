package com.example.zookeeper.funtion;

import com.example.zookeeper.ZKConst;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FIFOQueue implements Watcher {

    ZooKeeper zk;

    String root;

    public FIFOQueue(String root,String connectString) throws IOException, InterruptedException, KeeperException {
        this.root = root;
        zk = new ZooKeeper(connectString,0B11101010011000,this);
        Stat stat = zk.exists(root,false);
        if (Objects.isNull(stat)) {
            zk.create(root,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
    }

    public void producer(int i) throws KeeperException, InterruptedException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        zk.create(root+"/element",bb.array(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public String minNode(List<String> childrens) {
        List<Integer> sequential = childrens.parallelStream()
                .map(node->Integer.valueOf(node.substring(7)))
                .collect(Collectors.toList());
        int size = sequential.size();
//        System.out.println(Arrays.toString(sequential.toArray()));
        for (int i = 0B0; i < sequential.size(); i++) {
//            for (int j = size-1; j > i; j--) {
            for (int j = i; j < size; j++) {
                int temp;
                if (sequential.get(i) > sequential.get(j)) {
                    temp = sequential.get(i);
                    sequential.set(i,sequential.get(j));
                    sequential.set(j,temp);
                }
            }
        }
//        System.out.println(Arrays.toString(sequential.toArray()));
        String number = sequential.get(0B0)+"";
        StringBuilder sbs = new StringBuilder();
        for (int i = 0; i < (10 - number.length()); i++) {
            sbs.append("0");
        }
        sbs.append(number);
        return sbs.toString();
    }

    public int consumer() throws KeeperException, InterruptedException {
        while (true) {
            synchronized (this) {
                List<String> childrens = zk.getChildren(root,true);
                if (childrens.isEmpty()) {
                    this.wait();
                } else {
                    String min = this.minNode(childrens);
                    byte[] data = zk.getData(root+"/element"+min,false,null);
                    zk.delete(root+"/element"+min,-1);
                    ByteBuffer bb = ByteBuffer.wrap(data);
                    return bb.getInt();
                }

            }
        }
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        synchronized (this) {
            this.notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        /*为了省事，所有异常都直接向上抛了*/
        FIFOQueue fifoQueue = new FIFOQueue("/FIFO-QUEUE",ZKConst.CONNECT_STRING);
        for (int i = 0; i < 10; i++) {
            fifoQueue.producer(10+i);
        }
        for (int i = 0; i < 10; i++) {
            int n = fifoQueue.consumer();
            System.out.println("Item : "+n);
        }
    }
}

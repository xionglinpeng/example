package com.example.zookeeper.multiop;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.OpResult;
import org.apache.zookeeper.ZooKeeper;

import java.util.Arrays;
import java.util.List;

public class OpTest {

    private ZooKeeper zk;

    Op deleteZnode(String z) {
        return Op.delete(z,-1);
    }


    List<OpResult> results;

    {
        try {
            results = zk.multi(Arrays.asList(deleteZnode("/a/b"),deleteZnode("/a")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}

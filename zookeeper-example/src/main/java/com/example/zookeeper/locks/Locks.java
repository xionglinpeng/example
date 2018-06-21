package com.example.zookeeper.locks;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class Locks {

    public static void main(String[] args) {
        try {
            String connectString = "127.0.0.1:2181";
            int sessionTimeout = 15000;
            ZooKeeper zk = new ZooKeeper(connectString,sessionTimeout,System.out::println);

            String path = "/locks";

            Stat stat = zk.exists(path,false);
            if(stat == null){
                zk.create(path,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
            System.out.println("root : "+stat);


            String myZnode = zk.create(path+"/lock-",new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println("myZnode : "+myZnode);
            List<String> list = zk.getChildren(path,false);
            list.forEach(System.out::println);

            Stat statChild = zk.exists(myZnode,true);

            System.out.println("child : "+statChild);

            zk.delete(myZnode,-1);
            Thread.sleep(30000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

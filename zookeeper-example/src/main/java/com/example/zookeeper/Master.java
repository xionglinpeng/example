package com.example.zookeeper;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

@Data
public class Master implements Watcher {

    ZooKeeper zk;
    String hostPort;

    Random random = new Random();
    String serverId = Integer.toHexString(random.nextInt());
    Boolean isLeader = false;

    AsyncCallback.StringCallback masterCreateCallback = (rc,path,ctx,name)->{
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                checkMaster();
                return;
            case OK:
                isLeader = true;
                break;
            default:
                isLeader = false;
                System.out.println(rc);
        }
        System.out.println("I'm "+(isLeader?"":"not ")+"the leader");
    };

    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws IOException{
        zk = new ZooKeeper(hostPort,15000,this);
    }

    void stopZK() throws InterruptedException{
        zk.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    void runForMaster(){
        while (true){
//            try {
//                String s = zk.create(
//                        //我们试着创建znode结点/master。如果这个znode结点存在，create就会失败.
//                        //同时我们想在/master结点的数据字段保存对应这个服务器的唯一ID。
//                        "/master",
//                        //数据字段只能存储字节数组类型的数据，所以我们将int型转换为一个字节数组
//                        serverId.getBytes(),
//                        //如之前所提到，我们使用开放的ACL策略。
//                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                        //我们创建的结点类型为EPHMERAL
//                        CreateMode.EPHEMERAL);
//                isLeader = true;
//                break;
//            } catch (KeeperException e) {
//                isLeader = false;
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            zk.create(
                    //我们试着创建znode结点/master。如果这个znode结点存在，create就会失败.
                    //同时我们想在/master结点的数据字段保存对应这个服务器的唯一ID。
                    "/master",
                    //数据字段只能存储字节数组类型的数据，所以我们将int型转换为一个字节数组
                    serverId.getBytes(),
                    //如之前所提到，我们使用开放的ACL策略。
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    //我们创建的结点类型为EPHMERAL
                    CreateMode.EPHEMERAL,
                    masterCreateCallback,
                    null);
//            if (checkMaster()){
//                break;
//            }
        }

    }

    AsyncCallback.DataCallback masterCheckCallback =
            (int rc, String path, Object ctx, byte[] data, Stat stat)->{
                switch (KeeperException.Code.get(rc)) {
                    case CONNECTIONLOSS:
                        checkMaster();
                        return;
                    case NONODE:
                        runForMaster();
                        return;
                }
            };

    void checkMaster(){
        zk.getData("/master",false,masterCheckCallback,null);
    }

//    boolean checkMaster() {
//        while (true) {
//            try {
//                Stat stat = new Stat();
//                byte[] data = zk.getData("/master",false,stat);
//                isLeader = new String(data).equals(serverId);
//                return true;
//            } catch (KeeperException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }




    public static void main(String[] args) throws Exception {

        String hostPort = "192.168.56.2:2181";
        Master m = new Master(hostPort);
        m.startZK();
        m.runForMaster();
        if(m.isLeader){
            System.out.println("I'm the leader");
            //wait for a bit
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is the leader");
        }
        m.stopZK();
    }
}

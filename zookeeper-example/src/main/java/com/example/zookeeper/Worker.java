package com.example.zookeeper;

import lombok.Data;
import lombok.NonNull;
import lombok.Synchronized;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
public class Worker implements Watcher {

    private ZooKeeper zk;
    private String hostPort;
    private String status;
    private String serverId = Integer.toHexString((new Random()).nextInt());

    public Worker(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZK() throws IOException{
        zk = new ZooKeeper(hostPort,15000,this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent.toString()+", " +hostPort);
    }

    AsyncCallback.StringCallback createWorkerCallback = (rc,path,ctx,name) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                System.out.printf("创建从节点%s网络链接失败\n",name);
                register();
                break;
            case OK:
                System.out.printf("创建从节点%s成功。\n",name);
                break;
            case NODEEXISTS:
                System.out.printf("从节点%s已存在。\n",name);
                break;
            default:
                System.err.printf("创建从节点%s发生了一些错误 : %s\n",name,KeeperException.Code.get(rc));
        }
    };

    public void register() {
        zk.create("/workers/worker-"+serverId,"Idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,createWorkerCallback,null);
    }

    AsyncCallback.StatCallback statusUpdateCallback = (rc,path,ctx,name)->{
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                updateStatus(ctx.toString());
                return;
        }
    };

    @Synchronized
    public void updateStatus(@NonNull String status) {
        if (status.equals(this.status)) {
            zk.setData("/workers/worker-"+serverId,status.getBytes(),-1,statusUpdateCallback,status);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        this.updateStatus(status);
    }

    public static void main(String[] args) throws Exception {
        Worker worker = new Worker(ZKConst.CONNECT_STRING);
        worker.startZK();
        worker.register();
        Thread.sleep(30000);
    }


}

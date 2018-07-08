package com.example.zookeeper;

import lombok.Data;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class Master implements Watcher {

    private static final Logger logger = LogManager.getLogger(Master.class);

    ZooKeeper zk;
    String hostPort;

    String serverId = Integer.toHexString(new Random().nextInt());
    Boolean isLeader = false;

    public Master(String hostPort){
        this.hostPort = hostPort;
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(hostPort,15000,this);
    }

    public void stopZK() throws InterruptedException {
        zk.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    AsyncCallback.StringCallback masterCreateCallback = (rc,path,ctx,name) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                asyncCheckMater();
                return;
            case OK:
                this.isLeader = true;
                break;
            default:
                this.isLeader = false;
        }
        System.out.println("我"+(this.isLeader?"":"没有")+"成为Leader。（"+code+"）");
    };

    AsyncCallback.DataCallback masterCheckCallback = (rc,path,ctx,name,stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                asyncCheckMater();
            case NONODE:
                asyncRunForMater();
                return;
        }
    };

    public void asyncCheckMater() {
        zk.getData("/master",false,masterCheckCallback,null);
    }

    public void asyncRunForMater(){
        zk.create("/master",serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,masterCreateCallback,null);
    }

    public boolean syncCheckMaster() throws KeeperException,InterruptedException {
        while (true) {
            Stat stat = new Stat();
            try {
                byte[] data = zk.getData("/master",false,stat);
                //如果获取的/master节点数据和当前进程的serverId相等，即表示当前进程为群首节点。
                this.isLeader = new String(data).equals(serverId);
                //已经检查完成了，直接返回true，退出循环。
                return true;
            } catch (KeeperException.NoNodeException e) {
                //节点不存在，即表示创建/master的请求zookeeper服务器未才处理，返回false，让其再次发生请求。
                return false;
            } catch (KeeperException.ConnectionLossException e) {
                System.out.println("获取/master节点连接丢失。");
            }
        }
    }


    public void syncRunForMaster() throws KeeperException,InterruptedException {
        while (true) {
            try {
                zk.create("/master",serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
                //创建成功，表示当前进制为群首结点，设置isLeader为true。
                this.isLeader = true;
                break;
            } catch (KeeperException.NodeExistsException e) {
                //节点以及存在异常，表示已有其他进程称为群首，设置isLeader为false。
                this.isLeader = false;
                break;
            } catch (KeeperException.ConnectionLossException e){
                System.out.println("创建/master节点连接丢失。");
                //连接丢失，可能zookeeper服务器没有处理/master节点的创建请求，或者已经处理了创建的请求，但是返回到客户端的连接丢失
                //所以，调用checkMaster()检查/master结点是否创建成功。
                if(this.syncCheckMaster()) break;
            }
        }
    }


    public static void asyncExec(boolean async) throws Exception{
        String hostPort = "192.168.56.2:2181,192.168.56.5:2181,192.168.56.5:2181";
        Master m = new Master(hostPort);
        m.startZK();
        if(async) {
            m.asyncRunForMater();
            Thread.sleep(3000);
        } else {
            m.syncRunForMaster();
            if(m.isLeader){
                System.out.println("我是leader节点。");
                Thread.sleep(60000);
            } else {
                System.out.println("我没有竞争到lerder节点。");
            }
            m.stopZK();
        }
    }

    public static void main(String[] args) throws Exception {
        Master.asyncExec(true);
    }
}

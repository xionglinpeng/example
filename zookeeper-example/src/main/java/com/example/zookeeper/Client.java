package com.example.zookeeper;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.omg.CORBA.portable.ValueOutputStream;
import sun.security.krb5.internal.PAData;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class Client implements Watcher{

    private ZooKeeper zk;
    private String hostPort;

    public Client(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZK() throws Exception{
        zk = new ZooKeeper(hostPort,15000,this);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    String queueCommand(String command) throws KeeperException ,InterruptedException{
        while (true) {
            String name = null;
            try {
                name = zk.create("/tasks/task-",command.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
                System.out.printf("Created task node %s successfully.\n",name);
                return name;
            } catch (KeeperException.NodeExistsException e) {
                System.out.printf("任务节点%s已经存在。\n",command);
            } catch (KeeperException.ConnectionLossException e) {
                System.out.printf("创建任务节点连接丢失，任务：%s。\n",command);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(ZKConst.CONNECT_STRING);
        client.startZK();

        String name = client.queueCommand("kks");

        System.out.printf("Created %s\n",name);
    }
}

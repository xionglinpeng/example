package com.example.zookeeper;

import org.apache.zookeeper.*;

public class Client implements Watcher{

    ZooKeeper zk;
    String hostPort;

    public Client(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() throws Exception{
        zk = new ZooKeeper(hostPort,15000,this);
    }

    String queueCommand(String command) throws Exception {
        String name = null;
        while (true){
            try {
                name = zk.create(
                        "/tasks/task-",
                        command.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT_SEQUENTIAL);
                return name;

            } catch (KeeperException.NodeExistsException e) {
                throw new Exception(name + " already appears to be running");
            } catch (KeeperException.ConnectionLossException e) {

            }
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("192.168.56.2:2181");
        client.startZK();

        String name = client.queueCommand("");

        System.out.println("Created "+name);
    }
}

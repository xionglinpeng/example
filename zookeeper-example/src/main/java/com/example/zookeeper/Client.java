package com.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.omg.CORBA.portable.ValueOutputStream;
import sun.security.krb5.internal.PAData;

import java.util.concurrent.ConcurrentHashMap;

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


    void submitTask(String task,TaskObject taskCtx) {
        taskCtx.setTask(task);
        zk.create("/tasks/task-",task.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,createTaskCallback,taskCtx);
    }

    AsyncCallback.StringCallback createTaskCallback = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    submitTask(((TaskObject)ctx).getTask(),(TaskObject)ctx);
                    break;
                case OK:
                    System.out.println("My created task name: "+name);
                    ((TaskObject)ctx).setTaskName(name);
                    watchStatus("/status/"+name.replace("/task/",""),ctx);
                    break;
                default:
                    System.out.println("Something went wrong"+KeeperException.create(KeeperException.Code.get(i))+path);

            }
        }
    };

    ConcurrentHashMap<String,Object> ctxMap = new ConcurrentHashMap<>();

    void watchStatus(String path,Object ctx) {
        ctxMap.put(path,ctx);
        zk.exists(path,statusWatcher,existsCallback,ctx);
    }

    AsyncCallback.DataCallback getDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {

        }
    };

    Watcher statusWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                assert watchedEvent.getPath().contains("/status/task-");
                zk.getData(watchedEvent.getPath(),false,getDataCallback,ctxMap.get(watchedEvent.getPath()));
            }
        }
    };

    AsyncCallback.StatCallback existsCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int i, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    watchStatus(path,ctx);
                    break;
                case OK:
                    if (stat != null) {
                        zk.getData(path,false,getDataCallback,null);
                    }
                    break;
                case NONODE:
                    break;
                default:
                    System.out.println("Something went wrong when checking if the status node exists : "+KeeperException.create(KeeperException.Code.get(i))+path);
            }
        }
    };

    public static void main(String[] args) throws Exception {
        Client client = new Client("192.168.56.2:2181");
        client.startZK();

        String name = client.queueCommand("");

        System.out.println("Created "+name);
    }
}

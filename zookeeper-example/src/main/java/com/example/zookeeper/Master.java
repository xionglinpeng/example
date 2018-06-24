package com.example.zookeeper;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            case NODEEXISTS:
                masterExists();
            default:
                isLeader = false;
                System.out.println(rc);
        }
        System.out.println("I'm "+(isLeader?"":"not ")+"the leader");
    };

    void masterExists() {
        zk.exists("/master",masterExistsWatcher,masterExistsCallback,null);
    }

    AsyncCallback.StatCallback masterExistsCallback = new AsyncCallback.StatCallback() {
        @Override
        public void processResult(int i, String s, Object o, Stat stat) {
            switch (KeeperException.Code.get(i)) {
                case CONNECTIONLOSS:
                    masterExists();
                    break;
                case OK:
                    //也许在监视的时候，主节点被删除了（是否删除由）stat == null判断，重新竞选主节点。
                    if (stat == null) {
                        runForMaster();
                    }
                    break;
                default:
                    checkMaster();
                    break;
            }
        }
    };

    /**
     * 监视主节点，如果主节点被删除了，会触发NodeDeleted事件，当前线程取竞选主节点。
     */
    Watcher masterExistsWatcher = e -> {
        if(e.getType() == Event.EventType.NodeDeleted){
            assert "/master".equals(e.getPath());
            runForMaster();
        }
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

    List<String> workerList = new ArrayList<>();



    void getTasks() {
        zk.getChildren("/tasks",watchedEvent->{
            if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/tasks".equals(watchedEvent.getPath());
                getTasks();
            }
        },(rc,path,ctx,children)->{
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    if (children != null) {
                        assignTasks(children);
                    }
                    break;
                default:
                    System.out.println("getChiledren failed."+KeeperException.create(KeeperException.Code.get(rc))+path);
            }
        },null);
    }

    void deleteTask(String name) {

    }

    void assignTasks(List<String> tasks) {
        tasks.forEach(this::getTaskData);
    }



    void getTaskData(String task) {
        zk.getData("/tasks/"+task,false,(rc,path,ctx,data,stat)->{
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTaskData(ctx.toString());
                    break;
                case OK:

                    int worker = random.nextInt(workerList.size());
                    String designatedWorker = "";

                    String assignmentPath = "assign"+designatedWorker+"/"+ctx.toString();
                    createAssignment(assignmentPath,data);
                    break;
                default:
                    System.out.println("getChiledren failed." + KeeperException.create(KeeperException.Code.get(rc)) + path);
            }
        },task);
    }

    void createAssignment(String path,byte[] data) {
        zk.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,(rc,p,ctx,name)->{
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createAssignment(p,(byte[]) ctx);
                    break;
                case OK:
                    System.out.println("Task assigned correctly: "+name);
                    deleteTask(name.substring(name.lastIndexOf("/")+1));
                    break;
                case NODEEXISTS:
                    System.out.println("Task already assigned");
                    break;
                default:
                    System.out.println("Error when trying to assign task."+KeeperException.create(KeeperException.Code.get(rc))+p);
            }
        },data);
    }



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

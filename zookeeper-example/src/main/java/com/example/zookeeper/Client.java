//package com.example.zookeeper;
//
//import lombok.Data;
//import org.apache.zookeeper.*;
//import org.apache.zookeeper.data.Stat;
//import org.omg.CORBA.portable.ValueOutputStream;
//import sun.security.krb5.internal.PAData;
//
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Supplier;
//
//@Data
//public class Client implements Watcher{
//
//    private ZooKeeper zk;
//    private String hostPort;
//    private ConcurrentHashMap<String,Object> ctxMap = new ConcurrentHashMap<>();
//
//
//    public Client(String hostPort) {
//        this.hostPort = hostPort;
//    }
//
//    public void startZK() throws Exception{
//        zk = new ZooKeeper(hostPort,15000,this);
//    }
//
//    @Override
//    public void process(WatchedEvent watchedEvent) {
//        System.out.println(watchedEvent);
//    }
//
//    String queueCommand(String command) throws KeeperException ,InterruptedException{
//        while (true) {
//            String name = null;
//            try {
//                name = zk.create("/tasks/task-",command.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
//                System.out.printf("Created task node %s successfully.\n",name);
//                return name;
//            } catch (KeeperException.NodeExistsException e) {
//                System.out.printf("任务节点%s已经存在。\n",command);
//            } catch (KeeperException.ConnectionLossException e) {
//                System.out.printf("创建任务节点连接丢失，任务：%s。\n",command);
//            }
//        }
//    }
//
//    private AsyncCallback.StringCallback createTaskCallback = (rc,path,ctx,name) -> {
//        KeeperException.Code code = KeeperException.Code.get(rc);
//        TaskObject taskObject = (TaskObject)ctx;
//        switch (code) {
//            case CONNECTIONLOSS:
//                submitTask(taskObject::getTask,taskObject);
//                break;
//            case OK:
//                System.out.printf("我创建的任务名称：%s\n",name);
//                taskObject.setTaskName(name);
//                this.watchStatus("/status/"+name.replace("/tasks/",""),ctx);
//            default:
//                System.out.printf("提交任务发生了一些错误：%s\n",KeeperException.create(KeeperException.Code.get(rc)));
//        }
//
//    };
//
//    private AsyncCallback.DataCallback getDataCallback = (rc,path,ctx,data,stat) -> {
//
//    };
//
//    private Watcher statusWatcher = (watchedEvent) -> {
//        if (watchedEvent.getType() == Event.EventType.NodeCreated) {
//            assert watchedEvent.getPath().equals("/status/task-");
//            zk.getData(watchedEvent.getPath(),false,getDataCallback,ctxMap.get(watchedEvent.getPath()));
//        }
//    };
//
//    private AsyncCallback.StatCallback existsCallback = (rc,path,ctx,stat) -> {
//        KeeperException.Code code = KeeperException.Code.get(rc);
//        switch (code) {
//            case CONNECTIONLOSS:
//                this.watchStatus(path,ctx);
//                break;
//            case OK:
//                if (Objects.nonNull(stat)) {
//                    zk.getData(path,false,getDataCallback,null);
//                }
//            case NONODE:
//                break;
//            default:
//                System.out.printf("发生了一些错误：%s\n",KeeperException.create(KeeperException.Code.get(rc)));
//        }
//    };
//
//    public void submitTask(Supplier<String> task, TaskObject taskObject) {
//        taskObject.setTask(task.get());
//        zk.create("/tasks/task-",
//                task.get().getBytes(),
//                ZooDefs.Ids.OPEN_ACL_UNSAFE,
//                CreateMode.PERSISTENT_SEQUENTIAL,
//                createTaskCallback,
//                taskObject);
//    }
//
//    private void watchStatus(String path,Object ctx) {
//        ctxMap.put(path,ctx);
//        zk.exists(path,statusWatcher,existsCallback,ctx);
//    }
//
//
//    public static void main(String[] args) throws Exception {
//        Client client = new Client(ZKConst.CONNECT_STRING);
//        client.startZK();
//
//        String name = client.queueCommand("kks");
//
//        System.out.printf("Created %s\n",name);
//    }
//}

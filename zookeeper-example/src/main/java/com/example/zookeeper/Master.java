package com.example.zookeeper;

import lombok.Data;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Data
public class Master implements Watcher {

    private static final Logger logger = LogManager.getLogger(Master.class);

    private ZooKeeper zk;
    private String hostPort;

    private String serverId = Integer.toHexString(new Random().nextInt());
    private Boolean isLeader = false;
    private MasterStates state;

    private List<String> workerList = new ArrayList<>();


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

    private Watcher masterExistsWatcher = (watchedEvent) -> {
        //群首节点被删除，调用asyncRunForMater()竞选群首。
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            System.out.printf("主节点被删除：%s，开始竞选主节点...\n",watchedEvent.getType());
            assert "/master".equals(watchedEvent.getPath());
            asyncRunForMater();
        }
    };

    private AsyncCallback.StatCallback masterExistsCallback = (rc,path,ctx,stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                //连接丢失的情况下重试
                masterExists();
                break;
            case OK:
                /*
                因为在主节点创建的时候/master节点可能已经存在，所以需要调用masterExists()进行监听，
                回调到此处，此时，可能/master已经被删除了，所以我们需要判断stat参数是否为null（当节点不存在时，stat=null）,
                即表示/master节点被删除了，所以再次去竞选主节点。
                */
                if (Objects.isNull(stat)) {
                    //判断znode节点是否存在，不存在就竞选主节点。
                    state = MasterStates.RUNNING;
                    asyncRunForMater();
                }
                break;
            default:
                //如果发生意外情况，通过获取节点数据来检查/master是否存在。
                asyncCheckMater();
        }
    };

    private AsyncCallback.StringCallback masterCreateCallback = (rc,path,ctx,name) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                asyncCheckMater();
                return;
            case OK:
                System.out.printf("竞选主节点成功。（%s）\n",code);
                this.isLeader = true;
                break;
            case NODEEXISTS:
                System.out.printf("主节点存在，监听主节点。（%s）\n",code);
                state = MasterStates.NOTELECTED;
                //其他节点以及创建了/master，即竞选到了群首节点，客户端需要监视该节点
                masterExists();
                break;
            default:
                //发送了其他错误情况，记录错误日志，然后什么也不做。
                System.out.printf("创建主节点%s发生了一些错误 : %s\n",name,KeeperException.Code.get(rc));
        }
        System.out.println("我"+(this.isLeader?"":"没有")+"成为Leader。（"+code+"）");
    };

    private AsyncCallback.DataCallback masterCheckCallback = (rc,path,ctx,name,stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                asyncCheckMater();
            case NONODE:
                asyncRunForMater();
                return;
        }
    };

    /**
     * 监听/master节点
     * 用于在群首节点被删除（可能崩溃或手动删除等）时，其他备选主节点去竞选群首。
     */
    public void masterExists() {
        zk.exists("/master",masterExistsWatcher,masterExistsCallback,null);
    }

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
        Master m = new Master(ZKConst.CONNECT_STRING);
        m.startZK();
        if(async) {
            m.asyncRunForMater();
            Thread.sleep(30000);
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

    /**
     * 监视任务列表的变化
     */
    private Watcher tasksChangedWatcher = (watchedEvent) -> {
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            assert "/tasks".equals(watchedEvent.getType());
            this.getTasks();
        }
    };

    /**
     * 获取任务列表的回调
     */
    private AsyncCallback.ChildrenCallback tasksGetChildrenCallback = (rc,path,ctx,childrens) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                getTasks();
                break;
            case OK:
                if (Objects.nonNull(childrens)) {
                    this.assignTasks(childrens);
                }
                break;
            default:
                System.out.printf("获取任务子节点发生一些错误：%s\n",KeeperException.create(KeeperException.Code.get(rc)));
        }
    };


    private AsyncCallback.DataCallback taskDataCallback = (rc,path,ctx,data,stat) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                this.getTaskData((String)ctx);
                break;
            case OK:
                int worker = new Random().nextInt(this.workerList.size());
                String designatedWorker = this.workerList.get(worker);//从节点node名称：worker-serverId

                String assignmentPath = "/assign/"+designatedWorker+"/"+ctx;
                this.createAssignment(assignmentPath,data);
                break;
            default:
                System.out.printf("获取任务子节点信息发生一些错误：%s\n",KeeperException.create(KeeperException.Code.get(rc)));
        }
    };

    private AsyncCallback.StringCallback assignTaskCallback = (rc,path,ctx,name) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                this.createAssignment(path,(byte[])ctx);
                break;
            case OK:
                System.out.printf("任务分配正确%s\n",name);
                this.deleteTask(name.substring(name.lastIndexOf("/")+1));
                break;
            default:
                System.out.printf("创建任务发生一些错误：%s\n",KeeperException.create(KeeperException.Code.get(rc)));
        }
    };

    /**
     * 获得任务列表，这个任务列表中的任务表示为分配的任务，需要主节点获取其任务列表对从节点进行分配。
     * 绑定了watcher事件，如果watcher的NodeChildrenChanged事件被触发，即表示有任务新增获得删除，
     * 则从新调用getTasks()获取并监听。
     *
     * 在回调中，如果获取任务子节点成功，并且有任务子节点，那么就assignTasks()方法进行任务的分配。
     * 在分配之前获取每一个任务的任务信息，如果获取任务信息成功，就从从节点列表当中随机获取一个从节点
     * 作为标识（/assign/worker-id/task）为此从节点分配任务，一旦分配成功，则删除当前分配完成的任务，
     */
    public void getTasks() {
        zk.getChildren("/tasks",tasksChangedWatcher,tasksGetChildrenCallback,null);
    }

    /**
     * 分配任务
     * @param childrens
     */
    private void assignTasks(List<String> childrens) {
        //分配任务前，获得每一个任务节点信息
        childrens.forEach(this::getTaskData);
    }

    /**
     * 获得任务信息
     * @param task
     */
    private void getTaskData(String task) {
        zk.getData("tasks/"+task,false,taskDataCallback,task);
    }

    private void createAssignment(String path,byte[] data) {
        zk.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,assignTaskCallback,data);
    }

    private void deleteTask(String path) {
        try {
            zk.delete(path,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Master.asyncExec(true);
    }
}

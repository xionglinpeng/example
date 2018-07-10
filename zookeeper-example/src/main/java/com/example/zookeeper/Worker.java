package com.example.zookeeper;

import lombok.Data;
import lombok.NonNull;
import lombok.Synchronized;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
public class Worker implements Watcher {

    private ZooKeeper zk;
    private String hostPort;
    private String status;
    private String serverId = Integer.toHexString((new Random()).nextInt());
    private ChildrenCache workersCache;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10,30,3000,TimeUnit.DAYS,new LinkedBlockingDeque<>(100));

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

    private AsyncCallback.StringCallback createWorkerCallback = (rc,path,ctx,name) -> {
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

    private Watcher workersChangWatcher = (watchedEvent) -> {
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            System.out.printf("%s : 从节点发送变化。\n",watchedEvent.getType());
            //从节点发送变化，再次回去从节点，并监听。
            getWorkerList();
        }
    };

    private AsyncCallback.ChildrenCallback workersGetChildrenCallback = (rc,path,ctx,childrens) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                this.getWorkerList();
                break;
            case OK:
                System.out.printf("成功地得到了一个列表 : %d workers。\n",childrens.size());
                reassignAndSet(childrens);
                break;
            default:
                System.out.printf("%s : 获取从节点列表失败。\n",code);
        }
    };

    public void register() {
        zk.create("/workers/worker-"+serverId,"Idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL,createWorkerCallback,null);
    }

    public void getWorkerList() {
        zk.getChildren("/workers",workersChangWatcher,workersGetChildrenCallback,null);
    }

    public void reassignAndSet(List<String> childrens) {
        Set<String> toProcess = null;
        //检查从节点本地缓存是否不为null，为null,表示是第一次
        if (Objects.isNull(workersCache)) {
            System.out.printf("第一次获取到从节点列表 : %s，存储到本地的缓存中。\n",Arrays.toString(childrens.toArray()));
            //将当前从节点保存至本地缓存中
            workersCache = new ChildrenCache(childrens);
        } else {
            System.out.printf("删除并且设置 : %s\n", Arrays.toString(childrens.toArray()));
            //else 即不是第一次，则将当前获取的从节点与上一次获取存放在本地缓存的节点做对比，找到移除的节点
            //即是否有节点被移除了，将用于重新分配任务
            toProcess = workersCache.removeAndSet(childrens);
        }
        if (Objects.nonNull(toProcess)) {
            //重新分配任务
            toProcess.forEach(this::getAbsentWorkerTasks);
        }
    }

    /**
     * 重新分配任务
     * @param worker 工作节点
     */
    public void getAbsentWorkerTasks(String worker) {

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

    private Watcher newTaskWatcher = (watchedEvent) -> {
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            getTasks();
        }
    };

    private List<String> onGoingTasks = new ArrayList<>();


    private AsyncCallback.DataCallback taskDataCallback = (rc,path,ctx,data,stat) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        System.out.printf("开始执行任务 ：%s\n",new String(data));
    };

    private AsyncCallback.ChildrenCallback tasksGetChiledrenCallback = (rc,path,ctx,childrens) -> {
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case CONNECTIONLOSS:
                getTasks();
                break;
            case OK:
                if (Objects.nonNull(childrens)) {
                    //有分配任务，将任务放到线程池中执行
                    executor.execute(new Runnable() {
                        List<String> children;
                        AsyncCallback.DataCallback cb;

                        public Runnable init(List<String> children,AsyncCallback.DataCallback cb) {
                            this.children = children;
                            this.cb = cb;
                            return this;
                        }

                        @Override
                        public void run() {
                            synchronized (onGoingTasks) {
                                //循环任务列表
                                for (String task : children) {
                                    //如果当前的任务包含在正在执行的任务列表中...，就不要重复执行
                                    if (!onGoingTasks.contains(task)){
                                        System.out.printf("New Task: %s\n",task);
                                        //获取任务信息并执行
                                        zk.getData("/assign/worker-"+serverId+"/"+task,false,cb,task);
                                        //将正在执行的任务放到任务列表中，防止多次执行
                                        onGoingTasks.add(task);
                                    }
                                }
                            }
                        }
                    }.init(childrens,taskDataCallback));
                }
            default:
                System.out.printf("发生了一些错误：%s\n",KeeperException.create(KeeperException.Code.get(rc)));
        }
    };

    public void getTasks() {
        zk.getChildren("/assign/worker-"+serverId,newTaskWatcher,tasksGetChiledrenCallback,null);
    }


    public static void main(String[] args) throws Exception {
        Worker worker = new Worker(ZKConst.CONNECT_STRING);
        worker.startZK();
        worker.register();
//        worker.getWorkerList();
        Thread.sleep(30000);
    }


}

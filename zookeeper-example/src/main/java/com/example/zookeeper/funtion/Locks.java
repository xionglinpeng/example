package com.example.zookeeper.funtion;

import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class Locks implements Watcher {

    private final Object $lock = new Object[0];

    private static final int DEFAULT_ZK_SESSION_TIMEOUT = 15000;

    private ZooKeeper zk;

    private String myZNode;

    private static final String ROOT = "/locks";

    public Locks(String connectString){
        this(connectString,DEFAULT_ZK_SESSION_TIMEOUT);
    }

    public Locks(String connectString, int sessionTimeout){
        try {
            zk = new ZooKeeper(connectString,sessionTimeout,this);
            if (zk.exists(ROOT,false) == null) {
                zk.create(ROOT,new byte[0],ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        //所有竞争对象都会收到事件
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            //其中一个竞争对象删除了自身创建的临时顺序节点，即表示释放了锁，
            //所以，唤醒所有等待的竞争对象，让其去竞争锁。
            synchronized ($lock){
                $lock.notify();
            }
        }
    }

    public void getLock() throws KeeperException, InterruptedException {
        //获取所有子节点
        List<String> childrenNode = zk.getChildren(ROOT,false);
        //收集最小的子节点
        Optional<String> option = childrenNode.stream().sorted().findFirst();

        //lock_0000000016
        String minNode = ROOT + "/" + option.get();

        Stat stat = zk.exists(minNode, true);

        if (stat == null) {
            //获取的最小节点不存在，表示其他获取到锁的竞争对象抢到了锁，在当前竞争对象获取所有子节点的时候，还没有释放锁
            //而在进行最小子节点判断是否存在的时候，获取到锁的竞争对象已经删除最小子节点，并且很有可能在当前竞争对象获得
            //同步锁等待的时候就先一步调用了notify，导致当前竞争对象wait之后没有其他竞争对象唤醒，产生死锁。

            //所以，最小子节点不存在，就可以认为其他获取到锁的竞争对象已经释放了锁，不再等待，直接再次去竞争锁。
            getLock();
            return;
        }

        //如果最小的子节点与当前竞争对象的节点相同，即表示当前竞争对象获得锁
        if (myZNode.equals(minNode)) {
            this.doAction();
            //执行完成，释放锁(删除自己的节点)
            zk.delete(minNode,stat.getVersion());
        } else {
            //不相同，等待
            synchronized ($lock) {
                $lock.wait();
            }
            //等待被唤醒，表示其他竞争对象释放了锁，去竞争锁。
            getLock();
        }
    }

    public void doAction() {
        System.out.println(myZNode+" : For the lock.");
    }


    AsyncCallback.StringCallback stringCallback = (int code, String node, Object ctx, String name) ->{
            System.out.println(code+"<->"+node+"<->"+ctx+"<->"+name);

    };

    public void check() throws KeeperException, InterruptedException {
        //创建自己的临时顺序节点
//        zk.create(ROOT + "/lock_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new AsyncCallback.StringCallback() {
//            @Override
//            public void processResult(int code, String path, Object ctx, String node) {
////                code : 0
////                path : /locks/lock_
////                ctx : null
////                node : /locks/lock_0000000493
//                //注意，回调线程是一个单独的线性，并且所有的回调都是使用的同一个线程。
//                try {
//                    switch (KeeperException.Code.get(code)) {
//                        case CONNECTIONLOSS:
//                            check();
//                            break;
//                        case OK:
//                            myZNode = node;
//                            //去争取锁
//                            getLock();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        },null);
        this.myZNode = zk.create(ROOT + "/lock_", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        this.getLock();
    }

    public static void run(){
        Locks locks = new Locks("192.168.56.2:2181,192.168.56.4:2181,192.168.56.5:2181");
        try {
            locks.check();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        /*
        共享锁在同一个进程中很容易实现，但是在跨进程或者在不同 Server 之间就不好实现了。Zookeeper 却很容易实现这个功能，
        实现方式也是需要获得锁的 Server 创建一个 EPHEMERAL_SEQUENTIAL 目录节点，然后调用 getChildren方法获取当前的目
        录节点列表中最小的目录节点是不是就是自己创建的目录节点，如果正是自己创建的，那么它就获得了这个锁，如果不是那么它就
        调用 exists(String path, boolean watch) 方法并监控Zookeeper 上目录节点列表的变化，一直到自己创建的节点是列表
        中最小编号的目录节点，从而获得锁，释放锁很简单，只要删除前面它自己所创建的目录节点就行了。
        */
        Stream.generate(() -> new Thread(Locks::run)).limit(0B1010).parallel().forEach(Thread::start);
        //是主线程阻塞，因为zookeeper使用的是守护线程，主线程退出后，守护线程就关闭了。
        Thread.sleep(30000);
    }
}

package com.example.zookeeper.xiaoma;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据监视器
 */
public class DataMonitor implements Watcher,AsyncCallback.StatCallback {

    ZooKeeper zk;
    String znode;
    Watcher chainedWatcher;
    DataMonitorListener listener;

    boolean dead;

    byte prevData[];


    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        zk.exists(znode,true, this,null);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        KeeperException.Code code = KeeperException.Code.get(rc);
        switch (code) {
            case OK://节点存在
                exists = true;
                break;
            case NONODE://节点不存在
                exists = false;
                break;
            case SESSIONEXPIRED://会话超时
            case NOAUTH://没有严重
                dead = true;
                this.listener.closing(rc);
                return;
            default://发生了其他错误，重新观察znode节点
                zk.exists(znode,true,this,null);
                return;
        }
        byte b[] = null;
        if (exists) {//节点存在，获取节点数据
            try {
                b = zk.getData(znode,false,null);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //b == null，可能节点不存在，也可能节点存在，但是在获取节点数据时，节点被删除了，获得了null
        if ((b == null && b != prevData) || (b !=null && !Arrays.equals(prevData,b))) {
            this.listener.exists(b);
            prevData = b;
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        String path = watchedEvent.getPath();
        if (watchedEvent.getType() == Event.EventType.None) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    this.dead = true;
                    this.listener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        } else {
            if (Objects.nonNull(path) && path.equals(this.znode)) {
                zk.exists(znode,true,this,null);
            }
        }
        if (Objects.nonNull(this.chainedWatcher)) {
            this.chainedWatcher.process(watchedEvent);
        }
    }
}

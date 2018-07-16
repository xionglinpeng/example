package com.example.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;

public class CuratorTest {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("", new RetryPolicy() {
            @Override
            public boolean allowRetry(int i, long l, RetrySleeper retrySleeper) {
                return false;
            }
        });
        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/mypath",new byte[0]);
    }
}

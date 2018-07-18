package com.example.zookeeper.xiaoma;

public interface DataMonitorListener {

    /**
     * 节点的存在状态已经改变
     * @param data
     */
    void exists(byte[] data);

    /**
     * 会话不再有效
     * @param rc
     */
    void closing(int rc);
}

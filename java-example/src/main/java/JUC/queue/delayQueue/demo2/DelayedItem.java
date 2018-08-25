package JUC.queue.delayQueue.demo2;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedItem<K> implements Delayed {

    /**缓存键值*/
    private K key;

    /**缓存有效期*/
    private long liveTime;

    /**缓存到期时间*/
    private long removeTime;

    /**
     * 缓存到期时间的计算：当前时间毫秒数 + 缓存有效期
     * @param key 缓存键值
     * @param liveTime 缓存有效期
     */
    DelayedItem(K key,long liveTime){
        this.key = key;
        this.liveTime = liveTime;
        this.removeTime = System.currentTimeMillis()+liveTime;
    }

    /**
     * 计算缓存是否到期
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(removeTime - System.currentTimeMillis(),unit);
    }

    /**
     * 将缓存有效期时间最短的放在延时队列的最前面
     */
    @Override
    public int compareTo(Delayed o) {
        DelayedItem delayedItem = (DelayedItem)o;
        return (int)(liveTime - delayedItem.getLiveTime());
    }

    public K getKey() {
        return key;
    }

    public long getLiveTime() {
        return liveTime;
    }

    /**
     * 为了能够让DelayQueue将其已保存的key删除，需要重写实现
     * Delayed接口添加到DelayQueue的DelayedItem的hashCode函数。
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * 为了能够让DelayQueue将其已保存的key删除，需要重写实现
     * Delayed接口添加到DelayQueue的DelayedItem的equals函数。
     */
    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }
}

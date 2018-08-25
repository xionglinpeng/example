package JUC.queue.delayQueue.demo2;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * 向缓存添加内容时，给每一个key设定过期时间，系统自动将超过过期时间的key清除。
 *
 * 这个场景中几个点需要注意：
 * <ul>
 * <li>当向缓存中添加key-value对时，如果这个key在缓存中存在并且还没有过期，需要用这个key对应的新过期时间。</li>
 * <li>为了能够让DelayQueue将其已保存的key删除，需要重写实现Delayed接口添加到DelayQueue的DelayedItem的hashCode函数和equals函数。</li>
 * <li>当缓存关闭，监控程序也应关闭，因而监控线程应当用守护线程。</li>
 * </ul>
 * @param <K>
 * @param <V>
 */
public class Cache<K,V> {
    /**使用ConcurrentHashMap作为缓存map对象*/
    private ConcurrentHashMap<K,V> map = new ConcurrentHashMap<>();

    /**延时队列，用于监听缓存的有效期*/
    private DelayQueue<DelayedItem<K>> queue = new DelayQueue<>();

    /**
     * 新启用一个线程监听缓存的有效期，如果缓存关闭了，
     * 需要将监听线程也关闭，所以设置为守护线程。
     */
    Cache(){
        Thread thread = new Thread(this::dameonCheckOverdueKey);
        thread.setDaemon(Boolean.TRUE);
        thread.start();
    }

    /**
     * 添加数据到缓存<code>ConcurrentHashMap</code>中，
     * 同时创建一个<code>Delayed</code>对象用于监听缓存
     * 的有效期。如果缓存数据对应key值已经存在，则重设有效期。
     * @param k 缓存key
     * @param v 缓存value
     * @param liveTime 缓存有效期
     * @see DelayedItem
     */
    public void put(K k,V v,long liveTime){
        V value = map.put(k,v);
        System.out.printf("%s  :  %s     :  %d\n",k,value,liveTime);
        DelayedItem<K> delayedItem = new DelayedItem<>(k,liveTime);

        if (Objects.nonNull(value)) {
            queue.remove(delayedItem);
        }
        queue.put(delayedItem);
    }

    /**
     * 从延时队列中监听到期的缓冲，然后重缓冲中删除其缓冲。
     */
    public void dameonCheckOverdueKey(){
        try {
            for (;;) {
                //获取到期的缓冲
                DelayedItem<K> delayedItem = this.queue.take();
                System.out.printf("key = %s , validity date : %d\n",delayedItem.getKey(),delayedItem.getLiveTime());
                //缓存到期了，删除缓存
                this.map.remove(delayedItem.getKey());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

package JUC.queue.delayQueue.demo2;

import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //实例化了一个缓存，缓存对象构造中已经开始监听缓存到期了
        Cache<String,Object> cache = new Cache<>();
        int cacheNumber = 10;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            //向缓存中添加数据
            cache.put(i+"",1,random.nextInt(3000));
            //模拟缓存中存在，还没有到期
            if (random.nextInt(cacheNumber) > 7) {
                cache.put(i+"",1,random.nextInt(3000));
            }
        }

        Thread.sleep(5000);
    }
}

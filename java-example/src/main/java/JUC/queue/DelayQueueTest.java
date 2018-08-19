package JUC.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTest {

    public static void main(String[] args) throws InterruptedException {

        DelayQueue delayQueue = new DelayQueue();
        delayQueue.add(new Delayed() {
            // 返回与此对象相关的剩余延迟时间，以给定的时间单位表示。
            // 剩余延迟时间；零或负值指示延迟时间已经用尽

            @Override
            public long getDelay(TimeUnit unit) {


                System.out.println(Thread.currentThread().getName());
                return 1;
            }

            // 判断队列中元素的顺序谁前谁后。当前元素比队列元素后执行时，返回一个正数，
            // 比它先执行时返回一个负数，否则返回0.
            @Override
            public int compareTo(Delayed o) {
                return -1;
            }
        });

        Thread.sleep(100000);

//        delayQueue.take();

//        System.out.println(TimeUnit.SECONDS.convert(System.currentTimeMillis(),TimeUnit.MILLISECONDS));

    }
}


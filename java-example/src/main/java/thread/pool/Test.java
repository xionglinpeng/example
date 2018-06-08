package thread.pool;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by haolw on 2018/5/31.
 */
public class Test {

    public static void main(String[] args) {

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,4,1, TimeUnit.DAYS,queue);
        for(int i = 0 ;i < 10;i++){
            final int j = i;
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(new Random().nextInt(10000));
                        Thread.sleep(new Random().nextInt(10000));
                    }catch (Exception e) {
                        e.getMessage();
                    }


                    System.out.println("sss="+j);
                }
            });
        }
        executor.execute(new Runnable() {
            public void run() {
                System.out.println("sss="+"333333");
            }
        });
        System.out.println("dssdsd");
        executor.shutdown();
    }
}

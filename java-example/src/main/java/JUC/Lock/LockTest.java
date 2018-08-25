package JUC.Lock;

import sun.misc.Unsafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    public static void executor(Lock lock){
        try {
            System.out.println("----"+Thread.currentThread().getName());
            lock.lock();
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) throws Exception {
        final Lock lock = new ReentrantLock(false);

        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            new Thread(()->{
                executor(lock);
            }).start();
        }
//        Unsafe unsafe = Unsafe.getUnsafe();

//        unsafe.compareAndSwapInt();



    }
}

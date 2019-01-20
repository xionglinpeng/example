package com.xlp.example.redis.queue;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RedisDelayQueue<T> implements Queue<T> {

    private static final String QUEUE_KEY_PREFIX = "delay-queue:";

    private static final int DEFAULT_EXECUTE_NUMBER = 1;

    private ZSetOperations<Object, Object> queueOperations;

    private String queueKey;

    private Consumer<T> callback;

    private int executeNumber;

    private ThreadPoolTaskExecutor taskExecutor;

    public RedisDelayQueue(
            RedisTemplate<Object, Object> redisTemplate,
            String queueKey,
            Consumer<T> callback) {
        this(redisTemplate,QUEUE_KEY_PREFIX,queueKey,callback,DEFAULT_EXECUTE_NUMBER,null);
    }

    public RedisDelayQueue(
            RedisTemplate<Object, Object> redisTemplate,
            String queueKey,
            Consumer<T> callback,
            int executeNumber) {
        this(redisTemplate,QUEUE_KEY_PREFIX,queueKey,callback,executeNumber,null);
    }

    public RedisDelayQueue(
            RedisTemplate<Object, Object> redisTemplate,
            String queueKey,
            Consumer<T> callback,
            ThreadPoolTaskExecutor taskExecutor) {
        this(redisTemplate,QUEUE_KEY_PREFIX,queueKey,callback,DEFAULT_EXECUTE_NUMBER,taskExecutor);
    }

    public RedisDelayQueue(
            RedisTemplate<Object, Object> redisTemplate,
            String queueKey,
            Consumer<T> callback,
            int executeNumber,
            ThreadPoolTaskExecutor taskExecutor) {
        this(redisTemplate,QUEUE_KEY_PREFIX,queueKey,callback,executeNumber,taskExecutor);
    }

    public RedisDelayQueue(
            RedisTemplate<Object, Object> redisTemplate,
            String queueKeyPrefix,
            String queueKey,
            Consumer<T> callback,
            int executeNumber,
            ThreadPoolTaskExecutor taskExecutor) {
        this.queueOperations = redisTemplate.opsForZSet();
        this.queueKey = String.format(queueKeyPrefix+":%s", queueKey);
        this.executeNumber = executeNumber;
        this.callback = callback;
        this.taskExecutor = taskExecutor;
        loop();
    }

    /**
     *
     * @param message a
     * @param delayTime a
     */
    public void add(Serializable message, long delayTime){
        long mills = System.currentTimeMillis() + delayTime;
        queueOperations.add(queueKey,message,mills);
    }

    private void loop(){
        Runnable runnable = runnable();
        for (int i = 0; i < executeNumber; i++) {
            if (Objects.nonNull(taskExecutor)) {
                taskExecutor.execute(runnable);
            } else {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                thread.start();
            }
        }
    }

    private Runnable runnable(){
        return ()->{
            //noinspection InfiniteLoopStatement
            do {
                //设置offset为0，count为1，即每次只取一个到期的任务，将其它同时到期的任务分摊给其他线程或进程处理。
                Set<Object> tasks = queueOperations.rangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
                if (Objects.isNull(tasks) || tasks.isEmpty()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                Object task = tasks.iterator().next();
                //Redis的zrem方法是多线程的争抢任务的关键，它的返回值决定了当前实例有没有抢到任务，
                //因为loop方法可以被多个线程，多个进程调用，同一个任务可能会被多个进程线程抢到，通过zrem
                //来决定唯一属主。
                Long isScramble = queueOperations.remove(queueKey, task);
                Assert.notNull(isScramble,()->"redis delay queue by 'isScramble' result is null.");
                if (isScramble == 1) {
                    @SuppressWarnings("unchecked")
                    T message = (T) task;
                    //捕获异常，防止因为任务消费异常，导致线程退出
                    try {
                        callback.accept(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } while (true);
        };
    }


    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}

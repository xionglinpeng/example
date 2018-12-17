package com.redis.example.locks;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Method;

/**
 * 缓存环绕增强
 * {@link #invoke(MethodInvocation)}方法的实现与
 * {@link CacheInterceptor#invoke(MethodInvocation)}一样
 *
 * @see org.springframework.cache.interceptor.CacheInterceptor
 */
public class CacheLockInterceptor extends CacheLockAspectSupport implements MethodInterceptor {

    private StringRedisTemplate redisTemplate;

    public CacheLockInterceptor() {

    }

    public CacheLockInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
//        Object target = methodInvocation.getThis();
//        Method method = methodInvocation.getMethod();
//        Object[] arguments = methodInvocation.getArguments();
//        CacheLock distributedLock = methodInvocation.getMethod().getAnnotation(CacheLock.class);
//
//        int expire = distributedLock.expire();
//        TimeUnit timeUnit = distributedLock.timeUnit();
//        String lockKey = keyGenerator.generate(target,method,arguments);
//        try {
//            //采用原生API来实现分布式锁
//            Boolean success = redisTemplate.execute((RedisCallback<Boolean>) connection ->
//                    connection.set(lockKey.getBytes(), new byte[0],
//                            Expiration.from(expire, timeUnit),
//                            RedisStringCommands.SetOption.SET_IF_ABSENT)
//            );
//            if (Objects.isNull(success))
//                throw new RuntimeException("");
//            Object proceed = methodInvocation.proceed();
//            return proceed;
//        } finally {
//            redisTemplate.delete(lockKey);
//        }


        Method method = invocation.getMethod();

        CacheOperationInvoker aopAllianceInvoker = () -> {
            try {
                return invocation.proceed();
            }
            catch (Throwable ex) {
                throw new CacheOperationInvoker.ThrowableWrapper(ex);
            }
        };

        try {
            return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
        }
        catch (CacheOperationInvoker.ThrowableWrapper th) {
            throw th.getOriginal();
        }
    }




}

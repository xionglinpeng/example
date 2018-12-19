package com.redis.example.locks;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 缓存方面支持
 */
public abstract class CacheLockAspectSupport extends CacheAspectSupport {

    protected Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    @Override
    protected Object execute(CacheOperationInvoker invoker, Object target, Method method, Object[] args) {
        Class<?> targetClass = getTargetClass(target);
        CacheOperationSource cacheOperationSource = getCacheOperationSource();
        if (Objects.nonNull(cacheOperationSource)) {
            Collection<CacheOperation> operations = cacheOperationSource.getCacheOperations(method, targetClass);
            if (!CollectionUtils.isEmpty(operations))
                return execute(invoker,method,
                        new CacheOperationContexts(operations,method,args,target,targetClass));
        }
        return invoker.invoke();
    }

    private Object execute(CacheOperationInvoker invoker,Method method, CacheOperationContexts contexts){

        CacheLockOperationContext context = contexts.get(CacheLockOperation.class).iterator().next();

        //处理锁的增加
        processAddCacheLock(contexts.get(CacheLockOperation.class));
        Object o;
        try {
            o = invokeOperation(invoker);



        } finally {
            //处理锁的释放
            processReleaseCacheLock(contexts.get(CacheLockOperation.class));
        }
        return o;
    }


    /**
     * 处理增加缓存锁
     * @param contexts
     */
    private void processAddCacheLock(Collection<CacheLockOperationContext> contexts) {

        contexts.forEach(context -> {
            //判断是否满足condition条件
            if (this.isConditionPassing(context,null)) {


                performCacheLock(context,(CacheLockOperation) context.getOperation(),null);
            }
        });
    }

    /**
     * 执行添加缓存锁
     * @param context
     * @param operation
     * @param result
     */
    private void performCacheLock(CacheLockOperationContext context, CacheLockOperation operation, Object result) {
        Object key = context.generateKey(result);
        System.out.println(key);
    }


    /**
     * 处理释放缓存锁
     */
    private void processReleaseCacheLock(Collection<CacheLockOperationContext> contexts) {

    }




    /**
     * condition条件判断
     * @param context
     * @param result
     * @return
     */
    private boolean isConditionPassing(CacheLockOperationContext context,Object result) {
        boolean passing = context.isConditionPassing(result);
        if (!passing && logger.isTraceEnabled()) {
            logger.trace("Cache condition failed on method " + context.getMethod() +
                    " for operation " + context.getOperation());
        }
        return passing;
    }




    private class CacheOperationContexts{

        private final MultiValueMap<Class<? extends CacheOperation>,CacheLockOperationContext> contexts;

        public CacheOperationContexts(Collection<? extends CacheOperation> operations, Method method,
                                      Object[] args, Object target, Class<?> targetClass){
            this.contexts = new LinkedMultiValueMap<>(operations.size());
            operations.forEach(op->{
                contexts.add(op.getClass(),getOperationContext(op, method, args, target, targetClass));
            });
        }

        public Collection<CacheLockOperationContext> get(Class<? extends CacheOperation> operationClass){
            List<CacheLockOperationContext> result = contexts.get(operationClass);
            return Objects.nonNull(result)? result: Collections.emptyList();
        }
    }

    @Override
    protected CacheLockOperationContext getOperationContext(CacheOperation operation, Method method, Object[] args, Object target, Class<?> targetClass) {

        CacheOperationMetadata metadata = getCacheOperationMetadata(operation, method, targetClass);
        return new CacheLockOperationContext(metadata, args, target);
    }

    protected class CacheLockOperationContext extends CacheAspectSupport.CacheOperationContext{

        public CacheLockOperationContext(CacheOperationMetadata metadata, Object[] args, Object target){
            super(metadata, args, target);
        }

        @Override
        protected Object generateKey(Object result) {
            return super.generateKey(result);
        }

        @Override
        protected boolean isConditionPassing(Object result) {
            return super.isConditionPassing(result);
        }

    }
}

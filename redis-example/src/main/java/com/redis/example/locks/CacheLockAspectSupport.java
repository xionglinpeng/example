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
        Object o = context.generateKey(new Object());
        System.out.println(o);
        return invokeOperation(invoker);
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
    }
}

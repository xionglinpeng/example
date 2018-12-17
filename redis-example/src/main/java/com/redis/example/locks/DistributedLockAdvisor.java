package com.redis.example.locks;

import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.Objects;

public class DistributedLockAdvisor extends StaticMethodMatcherPointcutAdvisor {

    @Override
    public boolean matches(Method method, Class<?> aClass) {

        CacheLock distributedLock = method.getAnnotation(CacheLock.class);
        if (Objects.nonNull(distributedLock)) {
            System.out.println(String.format("distributed lock advisor by method = %s.",method));
            System.out.println(String.format("distributed lock advisor by aClass = %s.",aClass));
            System.out.println(String.format("distributed lock advisor by distributedLock = %s.",distributedLock));

            return true;
        }




        return false;
    }
}

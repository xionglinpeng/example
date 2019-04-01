package com.example.structure.proxy.chapt0;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WorkInvocationHandler implements InvocationHandler {

    private Object target;

    public WorkInvocationHandler(Object target) {
        this.target = target;
    }

    @SuppressWarnings("all")
    public static <T>T getProxyInstance(T target){
        InvocationHandler h = new WorkInvocationHandler(target);
        Class<?> classes = target.getClass();
        System.out.println(System.getSecurityManager());
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),classes.getInterfaces(),h);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("11111111111");
        Object object = method.invoke(target, args);
        System.out.println("22222222222");
        return object;
    }
}

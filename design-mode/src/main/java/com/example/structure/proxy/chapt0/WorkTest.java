package com.example.structure.proxy.chapt0;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;

public class WorkTest {

    public static void main(String[] args) throws Exception {
        IWork proxyWork = WorkInvocationHandler.getProxyInstance(new WorkImpl());
        proxyWork.startWork();


        byte[] as = ProxyGenerator.generateProxyClass("a", WorkImpl.class.getInterfaces());
//        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\dandelion\\Desktop\\a.class");){
//            fos.write(as);
//        }

    }
}

package com.example.jvm;

public class SimpleArgs {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.printf("参数%d:%s\n",i+1,args[i]);
        }
        System.out.printf("-Xmx%sM\n",Runtime.getRuntime().maxMemory()/1000/1000);
    }
}

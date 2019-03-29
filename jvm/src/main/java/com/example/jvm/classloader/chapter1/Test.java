package com.example.jvm.classloader.chapter1;

public class Test {

    static {
        i = 0;                 //给变量赋值可以正常编译通过
//        System.out.println(i); //这句编译器会提示“Illegal forward reference”
    }

    static int i = 1;
}

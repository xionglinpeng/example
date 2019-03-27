package com.example.jvm.classloader;

public class Parent {

    public int n = 9;

    static {
        System.out.println("Parent static code block.");
    }

    {
        System.out.println("Parent instance code block.");
    }

    public Parent() {
        System.out.println("Parent constructor function.");
    }

    public void func() {

    }

    public int getN() {
        return n;
    }
}

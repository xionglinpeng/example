package com.example.jvm.classloader;

public class Child extends Parent{

    public int n = 0;

    static {
        System.out.println("Child static code block.");
    }

    {
        System.out.println("Child instance code block.");
    }

    public Child() {
        System.out.println("Child constructor function.");
    }

    @Override
    public void func() {
        try {
            throw new Exception();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        System.out.println("my name is Child class.");
    }

    public int getN() {
        return n;
    }
}

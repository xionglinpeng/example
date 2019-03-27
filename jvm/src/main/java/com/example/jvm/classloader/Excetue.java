package com.example.jvm.classloader;

public class Excetue {

    public static void main(String[] args) {
        Child child = new Child();
        System.out.println(child.getN());
        child.func();
    }
}

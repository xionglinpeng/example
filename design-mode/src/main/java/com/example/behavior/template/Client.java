package com.example.behavior.template;

public class Client {

    public static void main(String[] args) {
        AbstractClass abstractClass1 = new ConcreteClass1();
        AbstractClass abstractClass2 = new ConcreteClass2();
        //调用模板方法
        abstractClass1.templateMethod();
        abstractClass2.templateMethod();
    }
}

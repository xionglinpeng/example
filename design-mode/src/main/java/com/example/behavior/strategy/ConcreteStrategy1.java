package com.example.behavior.strategy;

public class ConcreteStrategy1 implements Strategy{

    @Override
    public void doSomeing() {
        System.out.println("具体策略1的运算法则");
    }
}

package com.example.behavior.command;

public class ConcreteReceiver1 extends Receiver {

    @Override
    public void doSomeing() {
        System.out.println("业务逻辑1");
    }
}

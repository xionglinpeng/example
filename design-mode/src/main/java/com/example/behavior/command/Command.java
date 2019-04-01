package com.example.behavior.command;

public abstract class Command {

    protected Receiver receiver1 = new ConcreteReceiver1();
    protected Receiver receiver2 = new ConcreteReceiver2();

    public abstract void execute();
}

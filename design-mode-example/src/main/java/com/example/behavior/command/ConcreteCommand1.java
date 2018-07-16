package com.example.behavior.command;

public class ConcreteCommand1 extends Command {

    @Override
    public void execute() {
        super.receiver1.doSomeing();
        super.receiver2.doSomeing();
    }
}

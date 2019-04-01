package com.example.behavior.command;

public class ConcreteCommand2 extends Command {

    @Override
    public void execute() {
        super.receiver2.doSomeing();
        super.receiver1.doSomeing();
    }
}

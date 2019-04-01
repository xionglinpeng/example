package com.example.behavior.command;

public class Client {
    public static void main(String[] args) {
        Command command1 = new ConcreteCommand1();
        Command command2 = new ConcreteCommand2();
        Invoker invoker = new Invoker();
        invoker.setCommand(command2);
        invoker.doAction();

    }
}

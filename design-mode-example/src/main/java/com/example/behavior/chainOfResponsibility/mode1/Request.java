package com.example.behavior.chainOfResponsibility.mode1;

public class Request {

    private Level level;

    public Request(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}

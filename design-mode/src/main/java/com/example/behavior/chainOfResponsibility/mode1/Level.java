package com.example.behavior.chainOfResponsibility.mode1;

public class Level {

    private int level = 0;

    public Level(int level) {
        this.level = level;
    }

    public boolean above(Level level){
        if(this.level >= level.level){
            return true;
        }
        return false;
    }


}

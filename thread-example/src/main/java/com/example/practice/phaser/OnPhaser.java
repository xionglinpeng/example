package com.example.practice.phaser;

import java.util.concurrent.Phaser;

public class OnPhaser extends Phaser {

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        System.out.println(phase+"------"+registeredParties);
        return super.onAdvance(phase, registeredParties);
    }
}

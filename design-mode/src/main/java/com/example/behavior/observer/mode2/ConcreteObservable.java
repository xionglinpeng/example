package com.example.behavior.observer.mode2;

import java.util.Observable;

/**
 * Created by haolw on 2018/6/13.
 */
public class ConcreteObservable extends Observable {


    @Override
    public synchronized void setChanged() {
        super.setChanged();
        this.notifyObservers();
    }

    @Override
    public synchronized void clearChanged() {
        super.clearChanged();
    }
}

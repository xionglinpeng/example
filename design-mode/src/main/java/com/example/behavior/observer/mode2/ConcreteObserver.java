package com.example.behavior.observer.mode2;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by haolw on 2018/6/13.
 */
public class ConcreteObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        ConcreteObservable concreteObservable = (ConcreteObservable)o;
        System.out.println(this);
    }

}

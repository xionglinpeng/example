package com.example.behavior.observer.mode2;

import java.util.Observer;

/**
 * Created by haolw on 2018/6/13.
 */
public class ObserverClient {

    public static void main(String[] args) {
        ConcreteObservable observable = new ConcreteObservable();

        Observer observer1 = new ConcreteObserver();
        Observer observer2 = new ConcreteObserver();
        Observer observer3 = new ConcreteObserver();

        observable.addObserver(observer1);
        observable.addObserver(observer2);
        observable.addObserver(observer3);

        observable.setChanged();

    }
}

package com.example.create.prototype;

import java.util.ArrayList;

public class ConcretePrototype implements Cloneable {

    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected ConcretePrototype clone() {
        ConcretePrototype concretePrototype = null;
        try {
            concretePrototype = (ConcretePrototype)super.clone();
            this.list = (ArrayList<String>)list.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return concretePrototype;
    }
}

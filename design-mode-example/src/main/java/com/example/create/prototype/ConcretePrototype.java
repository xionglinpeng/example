package com.example.create.prototype;

public class ConcretePrototype implements Cloneable {

    @Override
    protected ConcretePrototype clone() {
        ConcretePrototype concretePrototype = null;
        try {
            concretePrototype = (ConcretePrototype)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return concretePrototype;
    }
}

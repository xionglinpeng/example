package com.example.behavior.iterator.common;

import java.util.Iterator;
import java.util.Vector;

public class ConcreteAggregate<E> implements Aggregate<E> {

    private Vector<E> vector = new Vector<>();

    @Override
    public void add(E e) {
        this.vector.add(e);
    }

    @Override
    public void remove(Object object) {

    }

    @Override
    public Iterator<E> iterator() {
        return new ConcreteIterator<>(this.vector);
    }
}

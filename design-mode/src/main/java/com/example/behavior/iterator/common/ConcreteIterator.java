package com.example.behavior.iterator.common;

import java.util.Iterator;
import java.util.Vector;

public class ConcreteIterator<E> implements Iterator<E> {

    private Vector<E> vector;

    public int cursor = 0B0;

    public ConcreteIterator(Vector<E> vector) {
        this.vector = vector;
    }

    @Override
    public boolean hasNext() {
        return this.cursor != this.vector.size();
    }

    @Override
    public E next() {
        E e = null;
        if (this.hasNext()) {
            e = this.vector.get(this.cursor++);
        }
        return e;
    }

}

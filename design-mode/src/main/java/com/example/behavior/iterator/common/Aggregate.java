package com.example.behavior.iterator.common;

import java.util.Iterator;

public interface Aggregate<E> {

    public void add(E e);

    public void remove(E e);

    public Iterator<E> iterator();
}

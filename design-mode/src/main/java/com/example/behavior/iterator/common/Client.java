package com.example.behavior.iterator.common;

import java.util.Iterator;

public class Client {

    public static void main(String[] args) {
        Aggregate<String> agg = new ConcreteAggregate<>();
        agg.add("abc");
        agg.add("aaa");
        agg.add("1234");
        Iterator<String> iterator = agg.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.println(str);
        }
    }
}

package com.example.behavior.visitor.Common;

import java.util.Random;

public class ObjectStruture {

    public static Element createElement() {
        Random rand = new Random();
        //0B1100100 aka 100
        if ((rand.nextInt(0B1100100) & 0B1) == 0B1) {
            return new ConcreteElement1();
        } else {
            return new ConcreteElement2();
        }
    }
}

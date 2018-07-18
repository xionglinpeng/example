package com.example.behavior.visitor.Common;

import java.util.Random;

public class Client {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Element el = ObjectStruture.createElement();
            el.accept(Visitor::new);
        }
    }
}

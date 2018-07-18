package com.example.behavior.visitor.Common;

import java.util.function.Supplier;

public class ConcreteElement1 extends Element {

    @Override
    public void doSomething() {
        System.out.println("ConcreteElement1");
    }

    @Override
    public void accept(Supplier<IVisitor> visitor) {
        visitor.get().visit(this);
    }
}

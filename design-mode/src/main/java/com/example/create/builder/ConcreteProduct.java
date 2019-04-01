package com.example.create.builder;

public class ConcreteProduct extends Builder {
    private Product product = new Product();
    @Override
    public void setPart() {
        //设置产品的零件，就定义了当产品如何组装。
    }

    @Override
    public Product builderProduct() {
        return this.product;
    }
}

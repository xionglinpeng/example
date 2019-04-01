package com.example.create.builder;

/**
 * 导演类，封装了不用模式的产品获取。
 * 将不同模式的产品封装在统一的导演类中，易于维护。
 */
public class Director {

    private Builder builder = new ConcreteProduct();

    public Product getProduct() {
        this.builder.setPart();
        return this.builder.builderProduct();
    }
}

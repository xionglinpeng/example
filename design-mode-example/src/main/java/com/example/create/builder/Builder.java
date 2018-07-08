package com.example.create.builder;

/**
 *
 */
public abstract class Builder {

    //设置零件/部件（一般实现了组装的方式）
    public abstract void setPart();

    //获得组装完成之后的产品
    public abstract Product builderProduct();
}

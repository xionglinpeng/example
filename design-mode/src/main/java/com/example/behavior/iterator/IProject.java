package com.example.behavior.iterator;

public interface IProject<E> {
    //增加项目
    public void add(E e);
    //从老板这里看到的就是项目信息
    public String getProjectInfo();
    //获得一个可以被遍历的对象
    public IProjectIterator<E> iterator();
}

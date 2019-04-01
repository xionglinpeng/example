package com.example.behavior.iterator;

import java.util.ArrayList;

public class Project<E> implements IProject<E> {

    //定义一个项目列表，所有的项目都放在这里
    private ArrayList<E> projectList = new ArrayList<>();
    //项目名称
    private String name;
    //项目成员数量
    private int num;
    //项目费用
    private int cost;

    public Project(){}

    //定义一个构造函数，把所有老板需要看到的信息存储起来
    public Project(String name, int num, int cost) {
        this.name = name;
        this.num = num;
        this.cost = cost;
    }
    //获得项目的信息
    @Override
    public String getProjectInfo() {
        String info = "";
        //获得项目的名称
        info = info+"项目名称是："+this.name;
        //获得项目人数
        info = info+"\t项目人数："+this.num;
        //项目费用
        info = info+"\t项目费用："+this.cost;
        return info;
    }

    @Override
    public void add(E e) {
        this.projectList.add(e);
    }

    //产生一个遍历对象
    @Override
    public IProjectIterator<E> iterator() {
        return new ProjectIterator<>(this.projectList);
    }
}

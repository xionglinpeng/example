package com.example.behavior.iterator;

import java.util.ArrayList;

public class ProjectIterator<E> implements IProjectIterator<E> {

    //所有的项目都放在ArrayList中
    private ArrayList<E> projectList;
    private int currentItem = 0b0;

    //构造函数传入projectList
    public ProjectIterator(ArrayList<E> projectList) {
        this.projectList = projectList;
    }

    //判断是否还有元素，必须实现
    @Override
    public boolean hasNext() {
        //定义一个返回值
        boolean b = true;
        if (this.currentItem >= projectList.size()){
            b = false;
        }
        return b;
    }

    //取得下一个值
    @Override
    public E next() {

        return this.projectList.get(this.currentItem++);
    }

    //删除一个对象
    public void remove() {
        //暂时没有使用到
    }
}

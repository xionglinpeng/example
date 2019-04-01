package com.example.behavior.visitor;

import java.util.ArrayList;
import java.util.List;

public class Client {

    //模拟出公司的人员情况，我们可以想象这个数据是通过持久层传递过来的。
    public static List<Employe> mockEmployee() {
        List<Employe> empList = new ArrayList<>();
        //产生张三这个员工
        CommonEmployee zhansan = new CommonEmployee();
        zhansan.setJob("编写Java程序，绝对的蓝领，苦工加搬运工。");
        zhansan.setName("张三");
        zhansan.setSalary(1800);
        zhansan.setSex(Employe.MALE);
        empList.add(zhansan);
        //产生李四这个员工
        CommonEmployee lisi = new CommonEmployee();
        lisi.setJob("页面美工，审美素质太不流行了！");
        lisi.setName("李四");
        lisi.setSalary(1900);
        lisi.setSex(Employe.FEMALE);
        empList.add(lisi);
        //再产生一个经理
        Manager wangwu = new Manager();
        wangwu.setPerformance("基本上是负值，但是我会拍马屁呀。");
        wangwu.setName("王五");
        wangwu.setSalary(18750);
        wangwu.setSex(Employe.MALE);
        empList.add(wangwu);
        return empList;
    }

    public static void main(String[] args) {
        for (Employe emp:mockEmployee()) {
            emp.accept(new Visitor());
        }
    }
}

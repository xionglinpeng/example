package com.example.chainOfResponsibility.mode2;

public class Client {

    public static void main(String[] args) {
        String username = "13980021561";
        ResponsibilityChainHandler email = new EmailRegister(username);
        ResponsibilityChainHandler mobile = new MobileRegister(username);

        //添加链接
        email.setNextHandler(mobile);



        email.chainHandler();


    }
}

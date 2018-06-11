package com.example.chainOfResponsibility.mode2;

public class EmailRegister extends ResponsibilityChainHandler {

    private String username;

    public EmailRegister(String username) {
        this.username = username;
    }

    @Override
    public boolean condition() {
        return this.username.contains("@");
    }

    @Override
    public void handler() {
        System.out.println("邮箱注册");
    }
}

package com.example.behavior.chainOfResponsibility.mode2;

public class MobileRegister extends ResponsibilityChainHandler {

    /**
     * 手机号码
     */
    public static final String PHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";

    private String username;

    public MobileRegister(String username) {
        this.username = username;
    }

    @Override
    public boolean condition() {
        return username.matches(PHONE);
    }

    @Override
    public void handler() {
        System.out.println("短信注册");
    }
}

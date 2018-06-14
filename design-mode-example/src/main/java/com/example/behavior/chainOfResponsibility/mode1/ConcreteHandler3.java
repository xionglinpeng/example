package com.example.behavior.chainOfResponsibility.mode1;

public class ConcreteHandler3 extends Handler {

    @Override
    protected Level getHandlerLevel() {
        return new Level(3);
    }

    @Override
    public Response response(Request request) {
        System.out.println("---请求由处理器3进行处理---");
        return null;
    }
}

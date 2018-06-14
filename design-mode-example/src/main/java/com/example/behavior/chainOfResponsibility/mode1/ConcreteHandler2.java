package com.example.behavior.chainOfResponsibility.mode1;

public class ConcreteHandler2 extends Handler {

    @Override
    protected Level getHandlerLevel() {
        return new Level(2);
    }

    @Override
    public Response response(Request request) {
        System.out.println("---请求由处理器2进行处理---");
        return null;
    }
}

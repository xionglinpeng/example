package com.example.chainOfResponsibility.mode1;

public abstract class Handler {

    private Handler nextHandler;
    //每个处理者都必须对请求做出处理
    public final Response handleRequest(Request request){
        Response response = null;
        if(this.getHandlerLevel().above(request.getLevel())){
            response = this.response(request);
        } else {
            if(this.nextHandler != null){
                this.nextHandler.handleRequest(request);
            } else {
                System.out.println("----没有合适的处理器---");
            }
        }
        return response;
    }
    //设置下一个处理这是谁
    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
    //每个处理者都有一个处理级别(相当于每一个处理者的处理条件)
    protected abstract Level getHandlerLevel();
    //每个处理者都必须实现这个处理任务
    public abstract Response response(Request request);
}

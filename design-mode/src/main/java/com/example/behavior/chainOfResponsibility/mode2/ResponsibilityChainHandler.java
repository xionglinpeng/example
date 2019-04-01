package com.example.behavior.chainOfResponsibility.mode2;

public abstract class ResponsibilityChainHandler {

    private ResponsibilityChainHandler nextHandler;

    public ResponsibilityChainHandler() {
    }

    public ResponsibilityChainHandler(ResponsibilityChainHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public ResponsibilityChainHandler setNextHandler(ResponsibilityChainHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }


    public void chainHandler(){
        if(this.condition()){
            this.handler();
        } else {
            if(this.nextHandler != null){
                this.nextHandler.handler();
            }else{
                System.out.println("----没有合适的处理器---");
            }
        }
    }



    public abstract boolean condition();

    public abstract void handler();

}

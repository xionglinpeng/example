package com.example.activemq.consumerFeatures;

/**
 * Created by haolw on 2018/6/12.
 */
public abstract class ConsumerFeaturesHandler {

    protected final String DEFAULT_TOPIC_NAME = "consumer-features";

    private ConsumerFeaturesHandler nextHandler;

    public void handler(int tag) {
        if(this.condition(tag)){
            this.execute();
        } else {
            if(this.nextHandler()) {
                this.next().handler(tag);
            } else {
                System.out.println("There are no more consumer features");
            }
        }
    }

    public ConsumerFeaturesHandler setNextHandler(ConsumerFeaturesHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public boolean nextHandler() {
        return this.nextHandler != null;
    }

    public ConsumerFeaturesHandler next() {
        return this.nextHandler;
    }

    protected abstract boolean condition(int tag);

    protected abstract void execute();
}

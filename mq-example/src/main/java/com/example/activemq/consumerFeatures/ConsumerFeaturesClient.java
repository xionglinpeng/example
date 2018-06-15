package com.example.activemq.consumerFeatures;

/**
 * Created by haolw on 2018/6/12.
 */
public class ConsumerFeaturesClient {

    public static void main(String[] args) throws Exception{
//        int tag = Integer.valueOf(args[0]);
//        ConsumerFeaturesHandler exclusiveCosnumer = new ExclusiveCosnumer();
//        ConsumerFeaturesHandler consumerPriority = new ConsumerPriority();
//        ConsumerFeaturesHandler consumerDispatchAsync = new ConsumerDispatchAsync();
//        ConsumerFeaturesHandler manageDurableSubscribers = new ManageDurableSubscribers();
//        ConsumerFeaturesHandler messageGroups = new MessageGroups();
//        exclusiveCosnumer
//        .setNextHandler(consumerPriority)
//        .setNextHandler(consumerDispatchAsync)
//        .setNextHandler(manageDurableSubscribers)
//        .setNextHandler(messageGroups)
//        ;
//        exclusiveCosnumer.handler(tag);

//        Foo foo = new Foo();
        System.out.println( Foo.AA());
        System.out.println( Foo.AA().equals(Foo.AA()));
    }
}

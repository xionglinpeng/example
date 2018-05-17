package com.example.activemq.amqp;

/**
 * Created by haolw on 2018/5/17.
 */
public class AmqpUtils {

    public static String env(String key,String defaultValue){
        String rc = System.getenv(key);
        if(rc == null)
            return defaultValue;
        return rc;
    }

    public static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }
}

package com.example.component;

import java.util.HashMap;
import java.util.Map;

public class Model {

    public static final String ATTRIBUTE_NAME = "data";

    public static final String STATUS = "status";

    public static final String ERROR = "error";

    public enum status{
        success,fail;
    }

    public static void success(Map<String,Object> result){
        result.put(Model.STATUS,status.success.toString());
    }

    public static void success(Map<String,Object> result,String key,Object value){
        result.put(Model.STATUS,status.success.toString());
        result.put(key,value);
    }

    public static Map<String,Object> success(){
        Map<String,Object> result = new HashMap<>();
        result.put(Model.STATUS,status.success.toString());
        return result;
    }

    public static Map<String,Object> success(String key,Object value){
        Map<String,Object> result = new HashMap<>();
        result.put(Model.STATUS,status.success.toString());
        result.put(key,value);
        return result;
    }

    public static void fail(Map<String,Object> result){
        result.put(Model.STATUS,status.fail.toString());
    }

    public static void fail(Map<String,Object> result,Object error){
        result.put(Model.STATUS,status.fail.toString());
        error(result,error);
    }

    public static Map<String,Object> fail(){
        Map<String,Object> result = new HashMap<>();
        result.put(Model.STATUS,status.fail.toString());
        return result;
    }

    public static Map<String,Object> fail(Object error){
        Map<String,Object> result = new HashMap<>();
        result.put(Model.STATUS,status.fail.toString());
        error(result,error);
        return result;
    }

    public static void error(Map<String,Object> result,Object error){
        result.put(Model.ERROR,error);
    }

    public static Map<String,Object> error(Object error){
        Map<String,Object> result = new HashMap<>();
        result.put(Model.ERROR,error);
        return result;
    }
}
package jcf;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by haolw on 2018/6/7.
 */
public class Test {



    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>(1 << 4,0.75F,false);
        map.put("1",1) ;
        map.put("2",2) ;
        map.put("3",3) ;
        map.put("4",4) ;
        map.put("5",5) ;
        System.out.println(map.get("3"));
//        System.out.println(map.get("2"));
        map.forEach((k,v)->{
            System.out.println(k+"=="+v);
        });
    }
}

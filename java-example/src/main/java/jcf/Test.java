package jcf;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by haolw on 2018/6/7.
 */
public class Test {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
//        LinkedHashMap<String, Integer> map = new LinkedHashMap<>(1 << 4,0.75F,false);
//        map.put("1",1) ;
//        map.put("2",2) ;
//        map.put("3",3) ;
//        map.put("4",4) ;
//        map.put("5",5) ;
//        System.out.println(map.get("3"));
////        System.out.println(map.get("2"));
//        map.forEach((k,v)->{
//            System.out.println(k+"=="+v);
//        });

//        System.out.println(Integer.valueOf(5).hashCode() & 3);
//        System.out.println(Integer.valueOf(7).hashCode() & 3);
//        System.out.println(Integer.valueOf(3).hashCode() & 3);
//        System.out.println(16<<1);
//        System.out.println(15>>1);
//        System.out.println(17>>1);
//        System.out.println(55%8);
//        System.out.println(55 & (8-1));
//        System.out.println(2*2*2*2*2*2*2);
//        System.out.println(tableSizeFor(64));


        Map<String,Object> map = new HashMap<>(2,0.75f);
        map.put("name","小红");

        // 1 0 0 0           1 0 0 0 0     0 1 1 1 1
        // 0 1 0 1  0        0 0 1 0 1     0 0 1 0 1
        // 1 1 0 1  1        0 1 1 0 1     0 1 1 0 1

    }
}

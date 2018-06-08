package object;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haolw on 2018/6/6.
 */
public class HashCodeTest {

    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.hashCode());
        user.setName("小明");
        System.out.println(user.hashCode());
        Map<String,Object> map = new HashMap<String,Object>();
        System.out.println(map.hashCode());
        map.put("name","小明");
        System.out.println(map.hashCode());
        map.put("age",20);
        System.out.println(map.hashCode());
    }
}

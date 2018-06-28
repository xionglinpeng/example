package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamFilter {

    public static void main(String[] args) {
        Stream<User> userStream = StreamCreate.users();

        List<User> list = userStream.filter(user -> user.getAge() > 25).collect(Collectors.toList());

        System.out.println(list);

        List<String> lists = new ArrayList<>();
        lists.add("1-dd-ff");
        lists.add("1-dd-ff-hh");
        lists.add("2-dd-ff");
        lists.add("2-dd-ff-cc");
        Stream<String> streams = lists.stream();
        List<String> ls = streams.filter(s->{
            boolean result = true;
            for (String ss : lists) {
                if(s.startsWith(ss) && !s.equals(ss)){
                    result = false;
                    break;
                }
            }
            return result;
        }).collect(Collectors.toList());
        System.out.println(ls);
    }
}

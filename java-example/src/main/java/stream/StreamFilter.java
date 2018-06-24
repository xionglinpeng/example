package stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamFilter {

    public static void main(String[] args) {
        Stream<User> userStream = StreamCreate.users();

        List<User> list = userStream.filter(user -> user.getAge() > 25).collect(Collectors.toList());

        System.out.println(list);
    }
}

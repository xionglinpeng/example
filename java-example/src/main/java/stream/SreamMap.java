package stream;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SreamMap {

    public static void main(String[] args) {
        Stream<User> userStream = StreamCreate.users();

        Stream<Integer> stream = userStream.map(User::getAge);

        List<Integer> list = stream.collect(Collectors.toList());
        System.out.println(list);
    }
}

package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamCreate {



    public static Stream<User> users() {
        List<User> users = new ArrayList<>();
        users.add(new User("William",23));
        users.add(new User("Andrew",21));
        users.add(new User("Sophia",26));
        users.add(new User("Thomas",29));
        users.add(new User("Jessica",24));
        users.add(new User("Jessica",20));
        return users.stream();
    }

    public static Stream<String> program() {
        Stream<String> stream = Stream.of("Tomcat","SpringCloud","Nginx","Zookeeper","ActiveMQ");
        return stream;
    }
}

package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamFlatMap {

    private Stream<Character> characterStream(String s) {
        List<Character> characters = new ArrayList<>();
        for (char c : s.toCharArray()) {
            characters.add(c);
        }
        return characters.stream();
    }

    public static void main(String[] args) {
        StreamFlatMap streamFlatMap = new StreamFlatMap();
        Stream<String> stream = Stream.of("Tomcat","SpringCloud","Nginx","Zookeeper","ActiveMQ");
        //多个stream合并
        List<Character> list = stream.flatMap(streamFlatMap::characterStream).collect(Collectors.toList());
        System.out.println(list);
    }
}

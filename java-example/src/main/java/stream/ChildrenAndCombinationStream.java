package stream;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 提取子流和组合流
 */
public class ChildrenAndCombinationStream {


    @Test
    public void limit(){
        //截取Stream前100个元素
        Stream<Double> stream = Stream.generate(Math::random).limit(100);
        System.out.println(stream.collect(Collectors.toList()));
    }

    @Test
    public void skip(){
        //丢弃Stream前面2个元素
        Stream<String> stream = StreamCreate.program().skip(2);
        System.out.println(stream.collect(Collectors.toList()));
    }

    @Test
    public void concat(){
        //合并program1和program2
        Stream<String> program1 = StreamCreate.program();
        Stream<String> program2 = StreamCreate.program();

        Stream<String> stream = Stream.concat(program1,program2);
        System.out.println(stream.collect(Collectors.toList()));
    }

    @Test
    public void distinct(){
        //distinct排除了重复元素
        Stream<String> stream = Stream.concat(StreamCreate.program(),StreamCreate.program()).distinct();
        System.out.println(stream.collect(Collectors.toList()));
    }

    @Test
    public void sorted(){
        System.out.println(StreamCreate.program().collect(Collectors.toList()));
        //sorted对元素进行排序
        Stream<String> stream = StreamCreate.program().sorted();

        System.out.println(stream.collect(Collectors.toList()));
    }

    @Test
    public void count(){
        long count = StreamCreate.program().count();
        System.out.println(count);
    }

    @Test
    public void max(){
        Optional<String> optional =  StreamCreate.program().max((s1,s2)->s1.length()-s2.length());

    }

    @Test
    public void min(){

    }

    @Test
    public void findFirst(){
        Optional<String> optional = StreamCreate.program().findFirst();
    }

    @Test
    public void findAny(){
        StreamCreate.program().findAny();
    }

    @Test
    public void anyMatch(){
        Stream<String> stream = StreamCreate.program();
        //stream中的所有元素只要有一个满足条件，则返回true。否则，返回true，
        boolean b = stream.anyMatch(s->s.equals("SpringCloud"));
        System.out.println(b);
    }

    @Test
    public void allMatch(){
        //用于判断流中的所有元素是否都满足指定条件,这个判断条件通过Lambda表达式传递给anyMatch,执行结果为boolean
        Stream<String> stream = StreamCreate.program();
        boolean b = stream.allMatch(s->s.length()>2);
        System.out.println(b);

    }

    @Test
    public void noneMatch(){
        Stream<String> stream = StreamCreate.program();
        //用于判断流中的所有元素是否都不满足指定条件,这个判断条件通过Lambda表达式传递给anyMatch,执行结果为boolean
        //stream中的所有元素都不满足条件，返回true，只要有一个满足，则返回false。
        boolean b = stream.noneMatch(s->s.equals("123"));
        System.out.println(b);
    }

    @Test
    public void optional(){
        List<String> list = new ArrayList<>();

        Optional<String> optional;
        optional = Optional.empty();
        optional = Optional.of("hello");
        optional = Optional.ofNullable("world");
        String value;

        //传递的函数参数不能为null，否则抛出NullPointerException
        //如果Optional没有值，则返回当前Optional对象
        //验证是否符合过滤条件，符合，则返回原Optional对象，如果不符合，则返回一个新的空Optional对象。
        optional = optional.filter(s->s.equals("hello"));

        //传递的函数参数不能为null，否则抛出NullPointerException
        //如果optional没有值，则返回一个空的新的optional对象。
        //消费T返回R，R继承Optional，即R类型必须为Optional类型
        //与map类似，不同的是：flatMap是自己手写包装计算，返回Optional类型，Optional的值类型必须与调用者一致，
        //例如：调用者是Optional<String>的泛型为String类型，那么R的泛型也必须为String类型。
        //注意flatMap，值R不能为null，否则抛出NullPointerException。
        //例如：optional.flatMap(v->null);
        //而map只是自己计算，有ofNullable方法进行了包装，值类型可以为任何类型。
        //可以换一个理解方式，就是map与flatMap封装的方式是一样的，只是map比flatMap封装的更彻底一下，查看起源码，可以发现其差不多。
        Optional<String> s = optional.flatMap(v->Optional.of("watcher"));
        System.out.println(s);

        //与Stream的map方法一样，消费T返回R，不同的是R会被ofNullable方法包装为一个新的optional。
        //传递的函数参数不能为null，否则抛出NullPointerException
        //如果optional没有值，则返回一个空的新的optional对象。
        Optional<Integer> length = optional.map(String::length);
        System.out.println(length.orElse(0));

        //如果值存在，则传递一个函数将其消费，否则不做任何操作
        optional.ifPresent(list::add);
        System.out.println(list);

        System.out.println("是否有值："+optional.isPresent());

        //获取值，如果没有值，则抛出异常java.util.NoSuchElementException: No value present
        value = optional.get();
        System.out.println("获取值："+value);

        //如果没有值，则返回声明的默认值
        value = optional.orElse("hello Optional");
        System.out.println("是否默认值 : "+value);

        //如果没有值，则返回计算的默认值
        value = optional.orElseGet(()->"hello Optional".substring(0,5));
        System.out.println("是否计算默认值 : "+value);

        //如果没有值，则抛出声明的异常，否则返回其值
        value = optional.orElseThrow(NullPointerException::new);
        System.out.println("有值，不抛异常："+value);

    }

    @Test
    public void reduce() {
        Stream<Integer> stream1 = Stream.of(1,3,456,234,4,234,678,234,6,234,87,7,23,657);
        //对类T进行二元操作，即参数类型与返回类型一致
        Optional<Integer> optional = stream1.reduce(Integer::sum);
        System.out.println(optional.orElse(0));

        Stream<Integer> stream2 = Stream.of(1,3,456,234,4,234,678,234,6,234,87,7,23,657);
        //对类T进行二元操作，即参数类型与返回类型一致
        Integer sum = stream2.reduce(0,Integer::sum);
        System.out.println(sum);

        Stream<User> users = StreamCreate.users();
        //对类T和U进行二元操作，即计算的参数类型与返回的结果不同。（可以并行计算）
        int l = users.reduce(0,(total,user)->{
            System.out.println("total = "+total);
            System.out.println("user = "+user);
            return total+user.getName().length();
        },(x, y)->{
            System.out.println(x);
            System.out.println(y);
            return x+y;});
        System.out.println(l);
    }

    @Test
    public void collectCollection() {
        Stream<String> stream = Stream.of("Speak","up","if","you","want","somebody");
//        //第一个参数指定收集的类型，例如TreeSet::new
//        //第二个参数指定收集的添加方法
//        //第三个参数指定并行收集的合并方法，只有在并行收集的情况下才会调用？
//        stream.collect(TreeSet::new,TreeSet::add,TreeSet::addAll);
//        //默认是ArrayList类型
//        stream.collect(Collectors.toList());
//        //默认是HashSet类型
//        stream.collect(Collectors.toSet());
//        //自己制定类型，例如：LinkedList
//        stream.collect(Collectors.toCollection(LinkedList::new));
//        //收集字符串拼接
//        stream.collect(Collectors.joining());
//        stream.collect(Collectors.joining(" "));
//        stream.collect(Collectors.joining(" ","(",")"));


//        DoubleSummaryStatistics s1 = stream.collect(Collectors.summarizingDouble(s->Double.parseDouble("123")));
//        LongSummaryStatistics s2 = stream.collect(Collectors.summarizingLong(s->Long.getLong("123")));
        IntSummaryStatistics s0 = stream.collect(Collectors.summarizingInt(String::length));
        System.out.println(s0.toString());
        //平均值；返回类型：double。
        System.out.println(s0.getAverage());
        //总数；返回类型：long。
        System.out.println(s0.getCount());
        //最大值；返回类型：int。
        System.out.println(s0.getMax());
        //最小值；返回类型：int。
        System.out.println(s0.getMin());
        //总和；返回类型：long。
        System.out.println(s0.getSum());
        //将一个新值记录到摘要信息中
        s0.accept(1);
        //将另一个IntSummaryStatistics的状态组合到这个状态中。
        s0.combine(s0);
    }

    @Test
    public void collectMap() {
        Stream<User> user = StreamCreate.users();
//        Map<String,Integer> map = user.collect(Collectors.toMap(User::getName,User::getAge));
        Map<String,User> map = user.collect(Collectors.toMap(User::getName,Function.identity()));
        Map<String,User> map1 = user.collect(Collectors.toMap(User::getName,Function.identity(),(a,b)->a,TreeMap::new));
        System.out.println(map);

        Stream<Locale> locale = Stream.of(Locale.getAvailableLocales());
//        Map<String,String> languageName = locale.collect(
//                Collectors.toMap(
//                        Locale::getDisplayLanguage,
//                        s->s.getDisplayLanguage(s),
//                        (o,n)->o));
//        languageName.forEach((k,v)-> System.out.println(k+"="+v));

        Map<String,Set<String>> countrylanguageSets = locale.collect(
                Collectors.toMap(
                        Locale::getDisplayLanguage,
                        s->Collections.singleton(s.getDisplayLanguage()),
                        (a,b)->{
                            Set<String> r = new HashSet<>(a);
                            r.addAll(b);
                            return r;
                        }));
        countrylanguageSets.forEach((k,v)-> System.out.println(k+"="+v));
    }

    @Test
    public void parallel() throws Exception {
        Set<String> threads = new HashSet<>();
        long startTime = System.currentTimeMillis();
        Stream.generate(Math::random).limit(1000).parallel().forEach(u->{
            try {
                Thread.sleep(100);
            }catch (Exception e) {}

            threads.add(Thread.currentThread().getName());
        });
        //25773 25785
        //100471
        System.out.println(System.currentTimeMillis()-startTime);
//        Thread.sleep(3000);
        System.out.println(threads.size());
        threads.forEach(System.out::println);
        System.out.println(threads.stream().distinct().collect(Collectors.toSet()));
    }
}

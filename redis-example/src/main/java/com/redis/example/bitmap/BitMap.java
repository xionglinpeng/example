package com.redis.example.bitmap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Component
public class BitMap implements CommandLineRunner {

    private static final String KEY = "bitmap";

    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostConstruct
    public void init(){
        stringRedisTemplate.delete(KEY);
    }

    @Override
    public void run(String... args) throws Exception {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Random random = new Random();
        for (int i = 0; i < 365; i++) {
            operations.setBit(KEY,i,isSign(random));
        }

        for (byte b : operations.get(KEY).getBytes(StandardCharsets.UTF_8)) {
            System.out.println(b+" = "+Integer.toBinaryString(b));
        }


        System.out.printf("签到字符串：%s。\n",1);
        System.out.printf("今天签到天数：%d天。\n",signNumber());
        System.out.printf("今年第96天到200天签到%d天。\n",signNumber1());
        System.out.printf("今年签到的第一天是第%d天。\n",signFirstDay());
        System.out.printf("96天之后签到的第一天是第%d天。\n",signFirstDayAfter100Day());
        Boolean isSign = operations.getBit(KEY, 200);
        assert Objects.nonNull(isSign);
        System.out.printf("第200天%s。\n",isSign?"签到了":"没有签到");

        System.out.println();
        for (int i = 0; i < 365; i++) {
            Boolean bit = operations.getBit(KEY, i);
            System.out.print(bit?1:0);
        }
        System.out.println();

        //从第三位开始（从0开始，包含第三位），取8为无符号数
        BitFieldSubCommands.BitFieldType uint8 = BitFieldSubCommands.BitFieldType.UINT_8;
        BitFieldSubCommands.Offset offset = BitFieldSubCommands.Offset.offset(3);
        //从第二十位开始（从0开始，包含第20位），将接下来两位用无符号数5替换
        BitFieldSubCommands.BitFieldType uint2 = BitFieldSubCommands.BitFieldType.unsigned(2);
        BitFieldSubCommands.Offset offset20 = BitFieldSubCommands.Offset.offset(20);
        //从第2位开始（从0开始，包含第2位），对接下来的4为无符号数+1，溢出折返-warp
        BitFieldSubCommands.BitFieldType uint4 = BitFieldSubCommands.BitFieldType.signed(4);
        BitFieldSubCommands.Offset offset2 = BitFieldSubCommands.Offset.offset(2);
        BitFieldSubCommands.BitFieldIncrBy.Overflow wrap = BitFieldSubCommands.BitFieldIncrBy.Overflow.WRAP;
        BitFieldSubCommands.BitFieldIncrBy.Overflow sat = BitFieldSubCommands.BitFieldIncrBy.Overflow.SAT;
        BitFieldSubCommands.BitFieldIncrBy.Overflow fail = BitFieldSubCommands.BitFieldIncrBy.Overflow.FAIL;

        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(uint8).valueAt(offset)
                .set(uint2).valueAt(offset20).to(5)
                .get(uint4).valueAt(offset2)
                .incr(uint4).valueAt(offset2).overflow(wrap).by(1)
                .incr(uint4).valueAt(offset2).overflow(sat).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(fail).by(1)
                .incr(uint4).valueAt(offset2).overflow(wrap).by(1)
                .get(uint4).valueAt(offset2);

        System.err.println("bitfield "+KEY+" get "+uint8.asString()+" "+offset.getValue());
        System.err.println("bitfield "+KEY+" set "+uint2.asString()+" "+offset20.getValue()+" "+5);
        System.err.println("bitfield "+KEY+" overflow wrap incrby "+uint4.asString()+" "+offset2.getValue()+" "+1);
        List<Long> longs = operations.bitField(KEY, bitFieldSubCommands);
        longs.forEach(l->{
            System.out.println(l+" : "+Integer.toBinaryString(l.intValue()));
        });
        s();
    }

    private void s(){
        //逻辑并
        RedisStringCommands.BitOperation and = RedisStringCommands.BitOperation.AND;
        RedisStringCommands.BitOperation not = RedisStringCommands.BitOperation.NOT;
        RedisStringCommands.BitOperation or = RedisStringCommands.BitOperation.OR;
        RedisStringCommands.BitOperation xor = RedisStringCommands.BitOperation.XOR;
        //与运算
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        Integer random = new Random().nextInt(1000);
        System.out.println(random);
        operations.set("{n}a",random);
        operations.setBit("{n}b",0,true);
        System.out.println(operations.get("{n}b"));
        for (byte aByte : operations.get("{n}b").toString().getBytes()) {
            System.out.println(aByte+" = "+Integer.toBinaryString(aByte));
        }

        Long andLong = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
            connection.bitOp(and, "{n}c".getBytes(), "{n}a".getBytes(),"{n}b".getBytes()));
        System.out.println(andLong);
        System.out.println(operations.get("{n}c"));
//        Long notLong = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
//            connection.bitOp(or, "".getBytes(), "".getBytes()));
//        Long orLong = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
//            connection.bitOp(or, "".getBytes(), "".getBytes()));
//        Long xorLong = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
//            connection.bitOp(xor, "".getBytes(), "".getBytes()));

        //CROSSSLOT Keys in request don't hash to the same slot
    }

    /**
     * bitCount
     */
    private Long signNumber(){
        return stringRedisTemplate.execute((RedisCallback<Long>)  connection ->
                connection.bitCount(KEY.getBytes(StandardCharsets.UTF_8))
        );
    }

    /**
     * bitCount
     */
    private Long signNumber1(){
        return stringRedisTemplate.execute((RedisCallback<Long>)  connection ->
                connection.bitCount(KEY.getBytes(StandardCharsets.UTF_8),96/8,200/8)
        );
    }

    /**
     * bitPos
     */
    private Long signFirstDay(){
        Long execute = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
            connection.bitPos(KEY.getBytes(StandardCharsets.UTF_8), true)
        );
        assert Objects.nonNull(execute);
        return ++execute;
    }

    /**
     * bitPos
     */
    private Long signFirstDayAfter100Day(){
        Range<Long> range = Range.from(Range.Bound.inclusive(96/8L)).to(Range.Bound.inclusive(365/8L));
        return stringRedisTemplate.execute((RedisCallback<Long>)  connection ->
                connection.bitPos(KEY.getBytes(StandardCharsets.UTF_8),true,range)
        );
    }







    private boolean isSign(Random random){
        return random.nextInt(0B10) == 1;
    }
}

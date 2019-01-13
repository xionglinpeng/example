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
import java.util.function.Consumer;
import java.util.function.Supplier;

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
        setbit();
        getbit();
        bitCount1();
        bitCount2();
        bitpos1();
        botpos2();
        bitfield();
        bitop();
    }

    /**
     * 模拟一年365天用户签到记录，1表示签到，0表示未签到。
     */
    private void setbit(){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Random random = new Random();
        for (int i = 0; i < 365; i++) {
            boolean b = random.nextBoolean();
            operations.setBit("bitmap",i, b);
            System.out.print(b?1:0);
        }
        System.out.println();
    }

    private void getbit(){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        for (int i = 0; i < 365; i++) {
            Boolean bitmap = operations.getBit("bitmap", i);
            assert bitmap != null;
            System.out.print(bitmap?1:0);
        }
        System.out.println();
    }

    /**
     * 查询今年全部的签到天数
     */
    private void bitCount1(){
        Long days = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitCount(stringRedisTemplate.getStringSerializer().serialize("bitmap"))
        );
        System.out.printf("今天年签到天数：%d天。\n", days);
    }

    /**
     * 查询第96天到200天的签到天数。
     * 注意：start和end是以字节为单位
     */
    private void bitCount2(){
        Long execute = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitCount(stringRedisTemplate.getStringSerializer().serialize("bitmap"),
                        96 / 8, 200 / 8)
        );
        System.out.printf("今年第96天到200天签到%d天。\n", execute);
    }


    /**
     * 查询今年签到的第一天
     */
    private void bitpos1(){
        Long execute = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitPos(stringRedisTemplate.getStringSerializer().serialize("bitmap"), true)
        );
        assert Objects.nonNull(execute);
        System.out.printf("今年签到的第一天是第%d天。\n", ++execute);
    }

    /**
     * 查询第96天之后签到的第一天
     * 注意：range是以字节为基本单位
     */
    private void botpos2(){
        Range<Long> range = Range.from(Range.Bound.inclusive(96/8L)).to(Range.Bound.inclusive(365/8L));
        Long execute = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
                connection.bitPos(stringRedisTemplate.getStringSerializer().serialize("bitmap"), true, range)
        );
        System.out.printf("96天之后签到的第一天是第%d天。\n", execute);
    }

    private void bitfield(){
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

        System.err.println("bitfield bitmap get "+uint8.asString()+" "+offset.getValue());
        System.err.println("bitfield bitmap set "+uint2.asString()+" "+offset20.getValue()+" "+5);
        System.err.println("bitfield bitmap overflow wrap incrby "+uint4.asString()+" "+offset2.getValue()+" "+1);
        List<Long> longs = stringRedisTemplate.opsForValue().bitField(KEY, bitFieldSubCommands);
        longs.forEach(l->{
            System.out.println(l+" : "+Integer.toBinaryString(l.intValue()));
        });
    }


    private void bitop(){
        ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
        //逻辑并
        RedisStringCommands.BitOperation and = RedisStringCommands.BitOperation.AND;
        //逻辑或
        RedisStringCommands.BitOperation not = RedisStringCommands.BitOperation.NOT;
        //逻辑异或
        RedisStringCommands.BitOperation or = RedisStringCommands.BitOperation.OR;
        //逻辑非
        RedisStringCommands.BitOperation xor = RedisStringCommands.BitOperation.XOR;
        //设置{o}:a = 0101
        operations.setBit("{bitop}:a",0,false);
        operations.setBit("{bitop}:a",1,true);
        operations.setBit("{bitop}:a",2,false);
        operations.setBit("{bitop}:a",3,true);
        //设置{o}:b = 0110
        operations.setBit("{bitop}:b",0,false);
        operations.setBit("{bitop}:b",1,true);
        operations.setBit("{bitop}:b",2,true);
        operations.setBit("{bitop}:b",3,false);

        System.out.println("bitOp:A = 0101");
        System.out.println("bitOp:B = 0110");
        //用于输出{bitop}:c
        Supplier<String> printBitOpC = ()->{
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                Boolean bit = operations.getBit("{bitop}:c", i);
                stringBuilder.append(bit?1:0);
            }
            return stringBuilder.toString();
        };
        //逻辑运算（不包括非）
        Consumer<RedisStringCommands.BitOperation> bitOp = operation-> {
            Long execute = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
                    connection.bitOp(operation, "{bitop}:c".getBytes(), "{bitop}:a".getBytes(), "{bitop}:b".getBytes()));
            System.out.print("实际结果："+printBitOpC.get()+"  ");
            System.out.println("execute = "+execute);
        };
        //逻辑运算：非
        Consumer<String> bitOpNot = key-> {
            Long execute = stringRedisTemplate.execute((RedisCallback<Long>) connection ->
                    connection.bitOp(not, "{bitop}:c".getBytes(), key.getBytes()));
            System.out.print("实际结果："+printBitOpC.get()+"  ");
            System.out.println("execute = "+execute);
        };
        //逻辑并运算
        System.out.print("AND:预期结果：0100,  ");
        bitOp.accept(and);
        //逻辑或运算
        System.out.print("OR :预期结果：0111,  ");
        bitOp.accept(or);
        //逻辑异或运算
        System.out.print("XOR:预期结果：0011,  ");
        bitOp.accept(xor);
        //逻辑非运算
        System.out.print("NOT:{bitop}:a->预期结果：1010,  ");
        bitOpNot.accept("{bitop}:a");
        System.out.print("NOT:{bitop}:b->预期结果：1001,  ");
        bitOpNot.accept("{bitop}:b");
        /*
        输出结果：
        bitOp:A = 0101
        bitOp:B = 0110
        AND:预期结果：0100,  实际结果：0100  execute = 1
        OR :预期结果：0111,  实际结果：0111  execute = 1
        XOR:预期结果：0011,  实际结果：0011  execute = 1
        NOT:{bitop}:a->预期结果：1010,  实际结果：1010  execute = 1
        NOT:{bitop}:b->预期结果：1001,  实际结果：1001  execute = 1
        */
        //CROSSSLOT Keys in request don't hash to the same slot
    }









    private boolean isBit(Random random){
        return random.nextInt(0B10) == 1;
    }
}

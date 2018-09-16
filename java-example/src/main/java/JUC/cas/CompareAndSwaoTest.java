package JUC.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class CompareAndSwaoTest {

    static class Target {
        public int value = 10;
    }

    public static void main(String[] args) throws Exception {

        Field field = Unsafe.class.getDeclaredField("theUnsafe");

        field.setAccessible(true);

        Unsafe unsafe = (Unsafe) field.get(null);

        Field valueField = Target.class.getDeclaredField("value");

        Target target = new Target();

        long valueOffset = unsafe.objectFieldOffset(valueField);

        System.out.println(unsafe.compareAndSwapInt(target,valueOffset,10,20));

        System.out.println(valueField.get(target));
    }
}

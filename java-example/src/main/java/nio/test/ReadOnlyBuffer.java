package nio.test;

import java.nio.ByteBuffer;

/**
 * Created by haolw on 2018/6/7.
 */
public class ReadOnlyBuffer {

    public static void main(String[] args) throws Exception{
        String message = "helloWorld";
        byte[] b = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(b);

        System.out.print("original  buffer out : ");
        for(int i = 0; i < buffer.capacity(); i++){
            System.out.print((char)buffer.get());
        }

        System.out.println();

        // 只读缓冲实际上是使用原缓冲对象的相关属性重新创建(new)了一个HeapByteBufferR对象，
        // 在这个对象中，所有的put方法都直接抛出了异常：throw new ReadOnlyBufferException();
        // 他们共享数据
        buffer.clear();
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.print("ready only buffer out : ");
        for(int i = 0; i < readOnlyBuffer.capacity(); i++){
            System.out.print((char)readOnlyBuffer.get());
        }

        System.out.println();

        Thread.sleep(1000);
        for(byte b1 : message.getBytes()){
            //抛出异常：Exception in thread "main" java.nio.ReadOnlyBufferException
            readOnlyBuffer.put(b1);
        }

    }
}

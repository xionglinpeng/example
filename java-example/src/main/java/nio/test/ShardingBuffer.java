package nio.test;

import java.nio.ByteBuffer;

/**
 * Created by haolw on 2018/6/7.
 * 包装、分片、数据共享
 */
public class ShardingBuffer {

    public static void main(String[] args) {
        byte[] b = new byte[10];
        ByteBuffer buffer = ByteBuffer.wrap(b);

        for(int i = 0; i < buffer.capacity(); i++){
            buffer.put((byte)i);
        }

        buffer.position(3).limit(7);
        ByteBuffer slice = buffer.slice();
        for(int i = 0; i < slice.capacity(); i++){
            slice.put(i,(byte)(slice.get()+100));
        }

        buffer.clear();//复位
        for(int i = 0; i < buffer.capacity(); i++){
            System.out.println(buffer.get());
        }
    }
}

package nio.test;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by haolw on 2018/6/8.
 * 分散读取
 */
public class ScatterRead {

    private static final String file = "C:\\Users\\lenovo\\Desktop\\Java NIO (中文版).pdf";

    public static void main(String[] args) throws Exception{

        FileOutputStream fos = new FileOutputStream(file);
        FileChannel channel = fos.getChannel();

        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(12);
        buffers[1] = ByteBuffer.allocate(1);
        buffers[2] = ByteBuffer.allocate(15);

        channel.read(buffers);


        for(ByteBuffer buffer : buffers){
            buffer.flip();

            for(int i = 0; i < buffer.capacity(); i++){
                System.out.print(buffer.get());
            }
            System.out.println();
        }

    }
}

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by haolw on 2018/5/29.
 */
public class NioTest {

    public static void main(String[] args) throws Exception{

        byte[] message = "hello nio .".getBytes();

        FileOutputStream fos = new FileOutputStream("C:\\Users\\lenovo\\Desktop\\nio.txt");

        FileChannel fileChannel = fos.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        for(byte msg : message){
            byteBuffer.put(msg);
        }

        System.out.println();

        byteBuffer.flip();

        fileChannel.write(byteBuffer);

    }
}

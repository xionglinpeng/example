import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by haolw on 2018/5/29.
 */
public class NioTest {

    public static void test() throws IOException{
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

    public static void test1() throws IOException{
        FileInputStream fis = new FileInputStream("C:\\Users\\lenovo\\Desktop\\数据结构和Java集合框架.pdf");
        FileChannel channel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        channel.read(byteBuffer);
        byteBuffer.flip();
        for(int i = 0; i < byteBuffer.limit(); i++){
            System.out.print((char)byteBuffer.get());
        }



    }

    public static void main(String[] args) throws Exception{
//        NioTest.test1();
        int d = 5200/1000;
        int c = 5200%1000;
        System.out.println(5200/1000);
        System.out.println(c);
    }
}

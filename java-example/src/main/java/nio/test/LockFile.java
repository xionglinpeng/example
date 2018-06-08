package nio.test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by haolw on 2018/6/8.
 * 说定文件
 */
public class LockFile {

    public static void main(String[] args) throws Exception{
        RandomAccessFile raf = new RandomAccessFile(
                "D:\\worksapce\\example\\java-example\\src\\main\\resources\\test\\http.txt","rw");
        FileChannel channel = raf.getChannel();
        long position = 1;
        long size = 1;
        boolean shared;

        //只能对可写的文件加锁
        FileLock lock = channel.lock(0,100,false);

        ByteBuffer buffer = ByteBuffer.allocateDirect(10);

        channel.read(buffer);

        buffer.flip();
//        System.out.println((char) buffer.get());
        for(int i = 0; i < buffer.limit();i++){
            System.out.print((char)buffer.get());
        }

//        FileInputStream fos = new FileInputStream(
//                "D:\\worksapce\\example\\java-example\\src\\main\\resources\\test\\http.txt");
//
//        FileChannel channel = fos.getChannel();
//
//        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
//        channel.read(buffer);
//        buffer.flip();
////        System.out.println(buffer.limit());
//        for(int i = 0; i < buffer.limit();i++){
//            System.out.print((char)buffer.get());
//        }

    }
}

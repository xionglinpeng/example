package nio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by haolw on 2018/6/8.
 * //连网和异步IO
 */
public class SyncNio {

    private static final ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        //To create a ServerSocketChannel.
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //Set ServerSocketChannel to non-blocking.
        socketChannel.configureBlocking(Boolean.FALSE);

        ServerSocket socket = socketChannel.socket();
        InetSocketAddress endpoint = new InetSocketAddress(8080);
        socket.bind(endpoint);

        //将新打开的 ServerSocketChannels 注册到 Selector上
        //register() 的第一个参数总是这个 Selector。第二个参数是 OP_ACCEPT，这里它指定我们想要监听 accept 事件，也就是在新的连接建立时所发生的事件。这是适用于 ServerSocketChannel 的唯一事件类型。
        //请注意对 register() 的调用的返回值。 SelectionKey 代表这个通道在此 Selector 上的这个注册。当某个 Selector 通知您某个传入事件时，它是通过提供对应于该事件的 SelectionKey 来进行的。SelectionKey 还可以用于取消通道的注册。
        SelectionKey selectionKey = socketChannel.register(selector,SelectionKey.OP_ACCEPT);

        //首先，我们调用 Selector 的 select() 方法。这个方法会阻塞，直到至少有一个已注册的事件发生。
        //当一个或者更多的事件发生时， select() 方法将返回所发生的事件的数量。
        int num = selector.select();

        //接下来，我们调用 Selector 的 selectedKeys() 方法，它返回发生了事件的 SelectionKey 对象的一个 集合 。
        Set<SelectionKey> keys = selector.selectedKeys();

        //我们通过迭代 SelectionKeys 并依次处理每个 SelectionKey 来处理事件。对于每一个 SelectionKey，
        // 您必须确定发生的是什么 I/O 事件，以及这个事件影响哪些 I/O 对象。
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            //readOps() 方法告诉我们该事件是新的连接。
            if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){

                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(Boolean.FALSE);
                //注册selector，并且是将SocketChannel用于读取
                SelectionKey newKey = sc.register(selector,SelectionKey.OP_READ);

                iterator.remove();

            }else if((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_READ){

                SocketChannel sc = (SocketChannel)key.channel();

                while (Boolean.TRUE){
                    echoBuffer.clear();

                    int i = sc.read(echoBuffer);
                    if(i < 0){
                        break;
                    }
                    echoBuffer.flip();

                    sc.write(echoBuffer);
                    assert num == 1;

                }
                iterator.remove();
            }
        }
    }
}

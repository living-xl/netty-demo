package base.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class Server {
    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            SelectionKey serverKey = serverChannel.register(selector, 0, null);
            log.debug("register serverKey - {}",serverKey);
            serverKey.interestOps(SelectionKey.OP_ACCEPT);

            serverChannel.bind(new InetSocketAddress(9999));

            while(true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    log.debug("select serverKey -{}",selectionKey);
                    ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                    SocketChannel sc = channel.accept();
                    log.debug("accept sc -{}",sc);


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

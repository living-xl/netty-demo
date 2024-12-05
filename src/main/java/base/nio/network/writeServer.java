package base.nio.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Slf4j
public class writeServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(9999));

            Selector selector = Selector.open();

            SelectionKey sscKey = ssc.register(selector, 0, null);
            sscKey.interestOps(SelectionKey.OP_ACCEPT);
            while (true){
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().isAcceptable()) {
                        SocketChannel sc = ssc.accept();

                        StringBuffer writeBuffer = new StringBuffer();
                        for (int i=1;i<=300000000;i++){
                            writeBuffer.append("a");
                        }
                        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(writeBuffer.toString());
                        while (byteBuffer.hasRemaining()) {
                            int length = sc.write(byteBuffer);
                            log.info("write length {}",length);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package base.nio.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static base.nio.bytebuffer.ByteBufferUtil.debugRead;

/**
 *
 */
@Slf4j
public class SingleThreadChannel {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try {
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            socketChannel.bind(new InetSocketAddress(9999));
            ArrayList<SocketChannel> channelList = new ArrayList<>();
            while (true){
                log.debug("connecting .....");
                //阻塞方法
                SocketChannel channel = socketChannel.accept();
                log.debug("connected .....");
                channelList.add(channel);
                channelList.forEach(cc->{
                    try {
                        log.debug("begin read,{}",channel);
                        cc.read(buffer);//阻塞方法
                        buffer.flip();
                        debugRead(buffer);
                        buffer.clear();
                        log.debug("after read,{}",channel);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

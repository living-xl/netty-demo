package base.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static base.bytebuffer.ByteBufferUtil.debugRead;

@Slf4j
public class SyncChannel {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try {
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            socketChannel.configureBlocking(false);//非阻塞模式
            socketChannel.bind(new InetSocketAddress(9999));
            ArrayList<SocketChannel> channelList = new ArrayList<>();
            while (true){
                //阻塞方法
                SocketChannel channel = socketChannel.accept();
                if(channel!= null){
                    channel.configureBlocking(false);
                    log.debug("connected .....{}",channel);
                    channelList.add(channel);
                }
                channelList.forEach(cc->{
                    try {
                        int length = cc.read(buffer);//阻塞方法
                        if(length!= 0 ){
                            buffer.flip();
                            debugRead(buffer);
                            buffer.clear();
                            log.debug("after read,{}",cc);
                        }

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

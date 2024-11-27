package base.network;

import base.bytebuffer.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static base.bytebuffer.ByteBufferUtil.debugRead;

@Slf4j
public class Server1 {
    public static void main(String[] args) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            SelectionKey serverKey = serverChannel.register(selector, 0, null);
            log.debug("register serverKey - {}",serverKey);
            serverKey.interestOps(SelectionKey.OP_ACCEPT);

            serverChannel.bind(new InetSocketAddress(9999));

            while(true){
                //select 方法在事件未处理时 不会进入阻塞 要么处理 要么取消
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isAcceptable()){
                        log.debug("select serverKey -{}",selectionKey);
                        ServerSocketChannel channel = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel sc = channel.accept();
                        log.debug("accept sc -{}",sc);
                        if(sc!=null){
                            sc.configureBlocking(false);
                            SelectionKey ccKey = sc.register(selector, 0, null);
                            log.debug("regist selector ccKey-{}",ccKey);
                            ccKey.interestOps(SelectionKey.OP_READ);
                        }

                    }else if(selectionKey.isReadable()){
                        log.debug("select serverKey read -{}",selectionKey);
                        SocketChannel cc = (SocketChannel)selectionKey.channel();
                        try {
                            int length = cc.read(buffer);//阻塞方法
                            log.debug("length - {}",length);
                            if(length > 0 ){
                                buffer.flip();
                                debugRead(buffer);
                                buffer.clear();
                                log.debug("after read,{}",cc);
                                cc.write(StandardCharsets.UTF_8.encode("你好!"));
                            }
                            else if(length<=-1){
                                log.debug("cc is close");
                                selectionKey.cancel();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            selectionKey.cancel();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

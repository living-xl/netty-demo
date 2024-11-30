package base.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import static base.bytebuffer.ByteBufferUtil.debugAll;
import static base.bytebuffer.ByteBufferUtil.debugRead;

/**
 * read数据边界问题
 */
@Slf4j
public class Server2 {
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
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            //把buffer作为副本注册到key中
                            SelectionKey ccKey = sc.register(selector, 0, buffer);
                            log.debug("regist selector ccKey-{}",ccKey);
                            ccKey.interestOps(SelectionKey.OP_READ);
                        }

                    }else if(selectionKey.isReadable()){
                        log.debug("select serverKey read -{}",selectionKey);
                        SocketChannel cc = (SocketChannel)selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        try {
                            int length = cc.read(buffer);//阻塞方法
                            log.debug("length - {}",length);
                            if(length > 0 ){
                                split(buffer);
//                                buffer.flip();
//                                debugRead(buffer);
                                if(buffer.position() == buffer.limit()){
                                    ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                    buffer.flip();
                                    newBuffer.put(buffer);
                                    selectionKey.attach(newBuffer);
                                }
//                                buffer.clear();
//                                log.debug("after read,{}",cc);
//                                cc.write(StandardCharsets.UTF_8.encode("你好!"));
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

    /**
     * 对buffer进行切割
     * @param resouce
     */
    private static void split(ByteBuffer resouce){
        resouce.flip();
        for (int i = 0; i < resouce.limit(); i++) {
            if(resouce.get(i) == '\n'){
                int length = i+1  - resouce.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(resouce.get());
                }
                debugAll(target);
            }
        }
        resouce.compact();
    }
}

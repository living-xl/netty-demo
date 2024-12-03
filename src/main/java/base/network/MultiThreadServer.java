package base.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static base.bytebuffer.ByteBufferUtil.debugAll;

@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) {
        try {
            Selector boss = Selector.open();

            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(9999));

            SelectionKey bossKey = ssc.register(boss, 0, null);
            bossKey.interestOps(SelectionKey.OP_ACCEPT);

            Worker worker = new Worker("worker-0");
            worker.register();
            while(true){
                boss.select();
                Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = channel.accept();
                        accept.configureBlocking(false);
                        log.debug("connected...{}",accept.getRemoteAddress());

                        log.debug("before register ...{}",accept.getRemoteAddress());
                        SelectionKey workerKey = accept.register(worker.selector, SelectionKey.OP_READ, null);
                        log.debug("after register ...{}",accept.getRemoteAddress());
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
   static class Worker implements Runnable{
        private Thread thread;
        private String name;
        private Selector selector;
        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }
        public void register() throws IOException {
            if(!start){
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                start = true;
            }
        }

        @Override
        public void run() {
            while(true){
                try {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey workerKey = iterator.next();
                        iterator.remove();
                        if (workerKey.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel) workerKey.channel();
                            log.debug("on read...{}",channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

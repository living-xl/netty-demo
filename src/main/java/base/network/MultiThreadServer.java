package base.network;

import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
            int cpu = Runtime.getRuntime().availableProcessors();
            log.debug("cpu - {}",cpu);
            Worker[] workers = new Worker[2];
            for (int i = 0; i < workers.length; i++) {
                workers[i]  = new Worker("worker-" + i);
            }
            AtomicInteger index = new AtomicInteger(0);
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
                        workers[index.addAndGet(1)%workers.length].register(accept);
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
        public ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

        public Worker(String name) {
            this.name = name;
        }
        public void register(SocketChannel sc) throws IOException {
            if(!start){
                thread = new Thread(this, name);
                selector = Selector.open();
                thread.start();
                start = true;
            }
            queue.add(()->{
                try {
                    sc.register(selector,SelectionKey.OP_READ,null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();

        }

        @Override
        public void run() {
            while(true){
                try {
                    selector.select();
                    Runnable task = queue.poll();
                    if(task!= null){
                        task.run();
                    }
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

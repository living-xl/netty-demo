package base.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import static base.bytebuffer.ByteBufferUtil.debugAll;

@Slf4j
public class AioDemo {
    public static void main(String[] args) {
        try {
            AsynchronousFileChannel asynIo = AsynchronousFileChannel.
                    open(Paths.get("data.txt"), StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(12);
            log.debug("start read...");
            asynIo.read(buffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.debug("completed read {}...",result);
                    buffer.flip();
                    debugAll(buffer);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.debug("read fail...");
                }
            });
            log.debug("end read ...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

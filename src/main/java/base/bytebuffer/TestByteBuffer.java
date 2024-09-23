package base.bytebuffer;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        try {
            FileChannel channel = new FileInputStream("data.txt").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true){
                int length = channel.read(buffer);
                log.debug("read length:{}",length);
                if(length == -1){break;}
                buffer.flip();//切换只读模式
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    System.out.println((char)b);

                }
                buffer.clear();//切换到写模式
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package base.bytebuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static base.bytebuffer.ByteBufferUtil.debugAll;

public class ScatteringReads {
    public static void main(String[] args) {
        try {
            FileChannel r = new RandomAccessFile("word.txt", "r").getChannel();
            ByteBuffer r1 = ByteBuffer.allocate(3);
            ByteBuffer r2= ByteBuffer.allocate(3);
            ByteBuffer r3= ByteBuffer.allocate(5);
            r.read(new ByteBuffer[]{r1,r2,r3});
            r1.flip();
            r2.flip();
            r3.flip();
            debugAll(r1);
            debugAll(r2);
            debugAll(r3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

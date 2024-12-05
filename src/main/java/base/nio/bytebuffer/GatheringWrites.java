package base.nio.bytebuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class GatheringWrites {
    public static void main(String[] args) {
        ByteBuffer w1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer w2 = StandardCharsets.UTF_8.encode("word");
        ByteBuffer w3 = StandardCharsets.UTF_8.encode("你好");
        try {
            FileChannel writer = new RandomAccessFile("word2.txt", "rw").getChannel();
            writer.write(new ByteBuffer[]{w1,w2,w3});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

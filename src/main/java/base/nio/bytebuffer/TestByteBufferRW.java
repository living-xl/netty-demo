package base.nio.bytebuffer;

import java.nio.ByteBuffer;
import static base.nio.bytebuffer.ByteBufferUtil.*;

public class TestByteBufferRW {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)0X61);
        debugAll(buffer);
        buffer.put(new byte[]{0x62,0x63,0x64});
        debugAll(buffer);
        buffer.flip();
        System.out.println(buffer.get());
        debugAll(buffer);
        buffer.compact();
        debugAll(buffer);
        buffer.flip();
        debugAll(buffer);
        
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println(buffer.get());

    }
}

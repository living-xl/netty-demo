package base.nio.bytebuffer;

import java.nio.ByteBuffer;

import static base.nio.bytebuffer.ByteBufferUtil.debugAll;

public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        byteBuffer.flip();

//        byteBuffer.get(new byte[4]);
//        debugAll(byteBuffer);
//        byteBuffer.rewind();
//        System.out.println((char)byteBuffer.get());
/*        System.out.println((char)byteBuffer.get());
        System.out.println((char)byteBuffer.get());
        byteBuffer.mark();
        System.out.println((char)byteBuffer.get());
        System.out.println((char)byteBuffer.get());
//        byteBuffer.reset();
        byteBuffer.rewind();
        System.out.println((char)byteBuffer.get());
        System.out.println((char)byteBuffer.get());*/
        System.out.println((char)byteBuffer.get(3));
        debugAll(byteBuffer);
    }
}

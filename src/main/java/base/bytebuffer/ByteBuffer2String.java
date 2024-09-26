package base.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static base.bytebuffer.ByteBufferUtil.debugAll;

public class ByteBuffer2String {
    public static void main(String[] args) {
        String str = "hello";
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(str.getBytes());
        debugAll(byteBuffer);

        ByteBuffer encode = StandardCharsets.UTF_8.encode(str);
        debugAll(encode);

        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
        debugAll(wrap);

        System.out.println(StandardCharsets.UTF_8.decode(encode));
        System.out.println(StandardCharsets.UTF_8.decode(wrap));
        byteBuffer.flip();
        System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));

    }
}

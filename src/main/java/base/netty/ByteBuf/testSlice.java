package base.netty.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static base.util.TestByteBuf.debugByteBuf;

public class testSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        debugByteBuf(buf);

        ByteBuf f1 = buf.slice(0, 5);
        f1.retain();
        ByteBuf f2 = buf.slice(5, 5);
        f2.retain();
        debugByteBuf(f1);
        debugByteBuf(f2);

        System.out.println("=============================");

        f1.setByte(0,'b');
        debugByteBuf(f1);
        debugByteBuf(buf);

        buf.release();
        debugByteBuf(f1);
        debugByteBuf(buf);
        f1.release();
        f2.release();
        debugByteBuf(buf);

    }
}

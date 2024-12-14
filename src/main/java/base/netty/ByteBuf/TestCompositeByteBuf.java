package base.netty.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static base.util.TestByteBuf.debugByteBuf;

public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer();
        buffer1.writeBytes(new byte[]{1,3,5,7,9});

        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.buffer();
        buffer2.writeBytes(new byte[]{2,4,6,8,0});

        CompositeByteBuf byteBufs = ByteBufAllocator.DEFAULT.compositeBuffer();
        byteBufs.addComponents(true,buffer1,buffer2);
        debugByteBuf(byteBufs);
    }
}

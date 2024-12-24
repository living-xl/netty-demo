package advanced.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TestLengthField {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,0,4,1,5),
                new LoggingHandler(LogLevel.DEBUG));

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        makePackage(buffer, "Hello,world!");
        makePackage(buffer, "Hi.");
        makePackage(buffer, "nihao.");
        channel.writeInbound(buffer);
    }

    private static void makePackage(ByteBuf buffer, String content) {
        byte[] bytes = content.getBytes();
        int length = bytes.length;
        buffer.writeInt(length);
        buffer.writeByte(1);
        buffer.writeBytes(bytes);
    }
}

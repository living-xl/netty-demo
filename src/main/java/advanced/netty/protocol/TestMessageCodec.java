package advanced.netty.protocol;

import advanced.netty.protocol.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LoggingHandler(),
//                new LengthFieldBasedFrameDecoder(
//                        1024,12,4,0,0),
                new MessageCodec()
        );
        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("living", "xl661628*", "胥亮");

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null,loginRequestMessage,buffer);

        embeddedChannel.writeInbound(buffer);
//        ByteBuf s1 = buffer.slice(0, 100);
//        ByteBuf s2 = buffer.slice(100, buffer.readableBytes()-100);
//        s1.retain();
//        embeddedChannel.writeInbound(s1);
//        embeddedChannel.writeInbound(s2);

//        embeddedChannel.writeAndFlush(loginRequestMessage);
    }
}

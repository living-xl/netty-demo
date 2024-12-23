package advanced.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Random;

public class FixLengthClient {
    public static void main(String[] args) {
        Bootstrap clientBs = new Bootstrap();
        clientBs.channel(NioSocketChannel.class);
        NioEventLoopGroup worker = new NioEventLoopGroup(1);
        clientBs.group(worker);
        clientBs.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ByteBuf buffer = ctx.alloc().buffer();
                        char c = 'a';
                        for (int i = 0; i < 10; i++) {
                            Random random = new Random();

                            buffer.writeBytes(makeFixLengthBytes((byte)c,random.nextInt(10),10));
                            c++;

                        }
                        ctx.writeAndFlush(buffer);
                        super.channelActive(ctx);
                    }
                });

            }
        });
        clientBs.connect(new InetSocketAddress("127.0.0.1",9999));
    }
    private static byte[] makeFixLengthBytes(byte a,int contentLength,int totalLength){
        byte[] bytes = new byte[totalLength];
        for (int i = 0; i < totalLength; i++) {
            if(i < contentLength){
                bytes[i] = a;
            }else {
                bytes[i] = "_".getBytes()[0];
            }
        }
        return bytes;
    }
}

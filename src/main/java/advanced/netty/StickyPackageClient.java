package advanced.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class StickyPackageClient {
    public static void main(String[] args) {
        send();
    }

    private static void send() {
        Bootstrap clientBs = new Bootstrap();
        clientBs.channel(NioSocketChannel.class);
        NioEventLoopGroup worker = new NioEventLoopGroup(1);
        clientBs.group(worker);
        clientBs.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        for (int i = 0; i < 10; i++) {
                            ByteBuf buffer = ctx.alloc().buffer(16);
                            buffer.writeBytes(new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
                            ctx.writeAndFlush(buffer);
                        }
                        super.channelActive(ctx);
                    }
                });
            }
        });
        clientBs.connect(new InetSocketAddress("127.0.0.1",9999));
    }
}

package advanced.chatdemo.test;


import advanced.chatdemo.protocol.MessageCodecSharable;
import advanced.netty.protocol.message.LoginRequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class TestChatServer {
    public static void main(String[] args) {
        try {
            Bootstrap client = new Bootstrap();
            client.channel(NioSocketChannel.class);
            client.group(new NioEventLoopGroup());
            client.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new MessageCodecSharable());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            LoginRequestMessage loginRequestMessage = new LoginRequestMessage("living", "xl661628*", "胥亮");
                            ctx.writeAndFlush(loginRequestMessage);
                            super.channelActive(ctx);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = client.connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

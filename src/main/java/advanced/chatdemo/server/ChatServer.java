package advanced.chatdemo.server;

import advanced.chatdemo.protocol.MessageCodecSharable;
import advanced.chatdemo.protocol.ProtocolLengthFieldFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        LoggingHandler LOG_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MSG_CODEC = new MessageCodecSharable();
        try {
            ServerBootstrap chatServer = new ServerBootstrap();
            chatServer.channel(NioServerSocketChannel.class);
            chatServer.group(boss, worker);
            chatServer.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(LOG_HANDLER);
                    ch.pipeline().addLast(new ProtocolLengthFieldFrameDecoder());
                    ch.pipeline().addLast(MSG_CODEC);
                }
            });
            ChannelFuture channelFuture = chatServer.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Throwable e) {
            log.error("chatServer error",e);
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

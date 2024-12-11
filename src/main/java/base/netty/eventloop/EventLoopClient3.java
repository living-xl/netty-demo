package base.netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class EventLoopClient3 {
    public static void main(String[] args) {
        try {
            NioEventLoopGroup group = new NioEventLoopGroup();
            ChannelFuture channelFuture = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    }).connect(new InetSocketAddress("localhost", 9999));
            log.debug("channelFuture - {}", channelFuture);
            Channel channel = channelFuture.sync().channel();
            new Thread(() -> {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    String in = scanner.nextLine();
                    if (in.equals("q")) {
                        channel.close();
                        log.debug("channel is close;");
                        break;
                    } else {
                        channel.writeAndFlush(in);
                    }
                }
            }).start();
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                    log.debug("channel is close--callback");
                    group.shutdownGracefully();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

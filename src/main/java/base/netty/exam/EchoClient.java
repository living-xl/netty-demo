package base.netty.exam;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

@Slf4j
public class EchoClient {
    public static void main(String[] args) {
        ChannelFuture connect = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("send data-{}", ((ByteBuf) msg).toString(Charset.defaultCharset()));
                                super.write(ctx, msg, promise);
                            }
                        });
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("revice data-{}", ((ByteBuf) msg).toString(Charset.defaultCharset()));
                                super.channelRead(ctx, msg);
                            }
                        });

                    }
                }).connect(new InetSocketAddress("127.0.0.1",9999));
        final Channel[] channel = new Channel[1];
        connect.addListeners(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                channel[0] = future.channel();
                log.debug("success-{}",channel[0]);
                channel[0].writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes()));
            }
        });
        while (true){
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.equals("q")){
                break;
            }else {
                log.debug("send-{}",channel[0]);
                channel[0].writeAndFlush(ByteBufAllocator.DEFAULT.buffer().writeBytes(line.getBytes()));
            }

        }
    }
}

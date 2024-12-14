package base.netty.exam;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

import static base.util.TestByteBuf.debugByteBuf;

@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("sendData",new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("write handle");
                                ByteBuf buffer = ctx.alloc().buffer();
                                try {
                                    buffer.writeBytes((ByteBuf)msg);
                                    log.debug("sendData-{}",buffer.toString(Charset.defaultCharset()));
                                    debugByteBuf(buffer);
                                    ((ByteBuf)msg).readerIndex(0);
                                    debugByteBuf((ByteBuf)msg);
                                    debugByteBuf((ByteBuf)msg);
                                    super.write(ctx, msg, promise);
                                }finally {
//                                    buffer.release();
                                }

                            }
                        });
                        ch.pipeline().addLast("reviceData",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = ctx.alloc().buffer();
                                try {

                                    buffer.writeBytes((ByteBuf)msg);
                                    log.debug("revice data-{}",buffer.toString(Charset.defaultCharset()));
                                    ctx.writeAndFlush(buffer);
                                    super.channelRead(ctx, msg);
                                }finally {
                                    buffer.release();
                                }

                            }
                        });


                    }
                }).bind(9999);
    }
}

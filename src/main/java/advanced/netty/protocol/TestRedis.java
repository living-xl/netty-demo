package advanced.netty.protocol;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class TestRedis {
    public static void main(String[] args) {
        final byte[] line = {13, 10};
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup worker = new NioEventLoopGroup(1);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(worker);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ByteBuf buffer = ctx.alloc().buffer();
//                        setCmd(buffer, line);
                        buffer.writeBytes("*2".getBytes());
                        buffer.writeBytes(line);
                        buffer.writeBytes("$4".getBytes());
                        buffer.writeBytes(line);
                        buffer.writeBytes("AUTH".getBytes());
                        buffer.writeBytes(line);
                        buffer.writeBytes(("$"+"quechubpass2.0".length()).getBytes());
                        buffer.writeBytes(line);
                        buffer.writeBytes("quechubpass2.0".getBytes());
                        buffer.writeBytes(line);
                        ctx.writeAndFlush(buffer);
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf revBuf = (ByteBuf) msg;
                        ByteBuf buffer = ctx.alloc().buffer();
                        if(revBuf.toString(Charset.defaultCharset()).startsWith("+OK")){
                            setCmd(buffer, line);
                        }
                        ctx.writeAndFlush(buffer);
                        super.channelRead(ctx, msg);
                    }
                });
            }
        });
        bootstrap.connect(new InetSocketAddress("192.168.25.122", 6379));

    }

    private static void setCmd(ByteBuf buffer, byte[] line) {
        buffer.writeBytes("*3".getBytes());
        buffer.writeBytes(line);
        buffer.writeBytes("$3".getBytes());
        buffer.writeBytes(line);
        buffer.writeBytes("set".getBytes());
        buffer.writeBytes(line);
        buffer.writeBytes("$4".getBytes());
        buffer.writeBytes(line);
        buffer.writeBytes("name".getBytes());
        buffer.writeBytes(line);
        buffer.writeBytes("$8".getBytes());
        buffer.writeBytes(line);
        buffer.writeBytes("zhangsan".getBytes());
        buffer.writeBytes(line);
    }
}

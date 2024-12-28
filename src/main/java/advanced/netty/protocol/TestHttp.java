package advanced.netty.protocol;

import com.sun.net.httpserver.HttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http2.Http2ServerUpgradeCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestHttp {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        try {
            ServerBootstrap httpServer = new ServerBootstrap();
            httpServer.channel(NioServerSocketChannel.class);
            httpServer.group(boss,worker);
            httpServer.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new HttpServerCodec());
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                            log.debug(msg.uri());
                            DefaultFullHttpResponse httpResponse =
                                    new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
                            byte[] bytes = "<h1>hello world!!!</h1>".getBytes();
                            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,bytes.length);
                            httpResponse.content().writeBytes(bytes);
                            ctx.writeAndFlush(httpResponse);

                        }
                    });

                }
            });
            ChannelFuture future = httpServer.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("server error.",e);
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

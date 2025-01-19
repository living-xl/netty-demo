package optimize;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConnectTimeOut {
    public static void main(String[] args) {
        //client 通过option来配置参数
        //server option 是配置serverSocketChannel
        //server childOption 是配置socketCannel
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.channel(NioSocketChannel.class);
            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            client.group(group);
            client.handler(new LoggingHandler());
            ChannelFuture connect = client.connect("127.0.0.1", 9999);
            connect.sync().channel().close().sync();
        } catch (Exception e) {
            log.debug("timeout");
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}

package base.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class HelloClient {
    public static void main(String[] args) {
        try{
            new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    }).connect(new InetSocketAddress("localhost",9999))
                    .sync()//阻塞方法，直到连接建立
                    .channel()//连接对象
                    .writeAndFlush("hello word.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

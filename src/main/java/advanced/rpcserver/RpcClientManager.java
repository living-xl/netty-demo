package advanced.rpcserver;

import advanced.chatdemo.protocol.MessageCodecSharable;
import advanced.chatdemo.protocol.ProtocolLengthFieldFrameDecoder;
import advanced.netty.protocol.message.RpcRequestMessage;
import advanced.rpcserver.handler.RpcResponseMessageHandler;
import advanced.rpcserver.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RpcClientManager {
    private static Channel channel = null;
    private static final Object Lock = new Object();
    private static final AtomicInteger sequenceid = new AtomicInteger();
    public static void main(String[] args) {
        HelloService helloService = getProxyService(HelloService.class);
        helloService.hello("张三");
        helloService.hello("李四");
    }
    public static <T> T getProxyService(Class<T> serviceClass){
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(sequenceid.addAndGet(1),
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args);
            getChannel().writeAndFlush(rpcRequestMessage);
            return null;
        });
        return (T)o;
    }


    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (Lock) {
            if (channel != null) {
                return channel;
            } else {
                initChannel();
                return channel;
            }
        }
    }

    private static void initChannel() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_RESMSG_HANDLER = new RpcResponseMessageHandler();

        Bootstrap clientBs = new Bootstrap();
        clientBs.group(worker);
        clientBs.channel(NioSocketChannel.class);
        clientBs.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolLengthFieldFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_RESMSG_HANDLER);
            }
        });
        try {
            ChannelFuture channelFuture = clientBs.connect(new InetSocketAddress("127.0.0.1", 9999));
            channel = channelFuture.sync().channel();
            channel.closeFuture().addListener(future -> {
                worker.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("rpc client error.", e);
        }
    }
}

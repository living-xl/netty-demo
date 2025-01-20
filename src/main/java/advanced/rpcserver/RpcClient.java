package advanced.rpcserver;

import advanced.chatdemo.protocol.MessageCodecSharable;
import advanced.chatdemo.protocol.ProtocolLengthFieldFrameDecoder;
import advanced.netty.protocol.message.RpcRequestMessage;
import advanced.rpcserver.handler.RpcResponseMessageHandler;
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

import java.net.InetSocketAddress;
@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_RESMSG_HANDLER = new RpcResponseMessageHandler();
        try {
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
            ChannelFuture channelFuture = clientBs.connect(new InetSocketAddress("127.0.0.1", 9999));
            Channel channel = channelFuture.sync().channel();
            RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(1,
                "advanced.rpcserver.service.HelloService",
                "hello",
                String.class,
                new Class[]{String.class},
                new Object[]{"张三"});
            ChannelFuture future = channel.writeAndFlush(rpcRequestMessage)
                    .addListener(promise->{
                        if(!promise.isSuccess()){
                            Throwable cause = promise.cause();
                            log.error("send rpc request error.",cause);
                        }
                    });
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("rpc client error.",e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}

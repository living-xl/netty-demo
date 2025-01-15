package advanced.chatdemo.server.handler;

import advanced.netty.protocol.message.PingMessage;
import advanced.netty.protocol.message.PongMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class PingMessageHandler extends SimpleChannelInboundHandler<PingMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
        log.info("revice {} ping message.",ctx.channel());
        ctx.channel().writeAndFlush(new PongMessage());
        ctx.fireChannelRead(msg);
    }
}

package advanced.chatdemo.server.handler;

import advanced.chatdemo.server.session.SessionFactory;
import advanced.netty.protocol.message.ChatRequestMessage;
import advanced.netty.protocol.message.ChatResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel toChannle = SessionFactory.getSession().getChannel(to);
        if(toChannle == null){
            ctx.writeAndFlush(new ChatResponseMessage(false,"发送方不存在或不在线！"));
        }else {
            toChannle.writeAndFlush(msg);
        }
    }
}

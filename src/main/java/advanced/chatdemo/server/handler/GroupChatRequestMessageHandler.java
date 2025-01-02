package advanced.chatdemo.server.handler;

import advanced.chatdemo.server.session.GroupSessionFactory;
import advanced.netty.protocol.message.GroupChatRequestMessage;
import advanced.netty.protocol.message.GroupChatResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        if(channels.size()<=0){
            log.debug("no memebers.");
            ctx.writeAndFlush(new GroupChatResponseMessage(false,groupName+"gourp have no memeber."));
        }else {
            channels.forEach(channel -> {
                channel.writeAndFlush(msg);
            });
            ctx.writeAndFlush(new GroupChatResponseMessage(true,"message send success"));
        }
    }
}

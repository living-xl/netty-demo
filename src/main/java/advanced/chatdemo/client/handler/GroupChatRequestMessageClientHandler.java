package advanced.chatdemo.client.handler;

import advanced.netty.protocol.message.GroupChatRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupChatRequestMessageClientHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String from = msg.getFrom();
        String content = msg.getContent();
        System.out.println(String.format("聊天室：%s---from:%s---content:%s",groupName,from,content));
    }
}

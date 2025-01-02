package advanced.chatdemo.server.handler;

import advanced.chatdemo.server.session.Group;
import advanced.chatdemo.server.session.GroupSessionFactory;
import advanced.netty.protocol.message.GroupCreateRequestMessage;
import advanced.netty.protocol.message.GroupCreateResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        Group group = GroupSessionFactory.getGroupSession().createGroup(groupName, members);
        if(group == null){
            ctx.writeAndFlush(new GroupCreateResponseMessage(true,groupName+"创建成功"));
            List<Channel> membersChannel = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
            membersChannel.forEach(channel -> {
                channel.writeAndFlush(new GroupCreateResponseMessage(true,"你已经被拉入"+groupName+"群组，可以开始聊天"));
            });
        }else{
            ctx.writeAndFlush(new GroupCreateResponseMessage(false,groupName+"已存在"));
        }
    }
}

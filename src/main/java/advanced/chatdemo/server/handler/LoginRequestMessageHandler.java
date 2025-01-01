package advanced.chatdemo.server.handler;

import advanced.chatdemo.server.service.UserServiceFactory;
import advanced.chatdemo.server.session.SessionFactory;
import advanced.netty.protocol.message.LoginRequestMessage;
import advanced.netty.protocol.message.LoginResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage loginResponseMessage;
        if(login){
            SessionFactory.getSession().bind(ctx.channel(),username);
            loginResponseMessage = new LoginResponseMessage(true,"登录成功！");
        }else {
            loginResponseMessage = new LoginResponseMessage(false,"登录失败！");
        }
        ctx.writeAndFlush(loginResponseMessage);
    }
}

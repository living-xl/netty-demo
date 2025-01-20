package advanced.rpcserver.handler;

import advanced.netty.protocol.message.RpcRequestMessage;
import advanced.netty.protocol.message.RpcResponseMessage;
import advanced.rpcserver.service.HelloService;
import advanced.rpcserver.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage rpcRequestMessage) throws Exception {
        RpcResponseMessage response = new RpcResponseMessage();
        try {
            HelloService service = (HelloService) ServicesFactory.getService(Class.forName(rpcRequestMessage.getInterfaceName()));
            Method method = service.getClass().getMethod(rpcRequestMessage.getMethodName(), rpcRequestMessage.getParameterTypes());
            Object invoke = method.invoke(service, rpcRequestMessage.getParameterValue());
            response.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            response.setExceptionValue(e);
        }
        response.setSequenceId(rpcRequestMessage.getSequenceId());
        ctx.writeAndFlush(response);
    }

//    public static void main(String[] args) throws Exception {
//        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(1,
//                "advanced.rpcserver.service.HelloService",
//                "hello",
//                String.class,
//                new Class[]{String.class},
//                new Object[]{"张三"});
//        HelloService service = (HelloService)ServicesFactory.getService(Class.forName(rpcRequestMessage.getInterfaceName()));
//        Method method = service.getClass().getMethod(rpcRequestMessage.getMethodName(), rpcRequestMessage.getParameterTypes());
//        Object invoke = method.invoke(service, rpcRequestMessage.getParameterValue());
//        System.out.println(invoke);
//    }
}

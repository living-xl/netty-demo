package base.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        //1 启动器 负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2 BossEventLoop,WorkerEventLoop(selector,thread)
                /**
                 * EventLoop:不断循环执行事件，由selector和thread组成
                 */
                .group(new NioEventLoopGroup())
                //3 选择服务器的ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                //4 boss 负责处理连接 worker(child)负责处理读写，决定了worker(child)能执行哪些操作（handler）
                .childHandler(
                        //5 channel 代表和客户端进行数据读写通道的Initializer处理化，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                //6 添加具体handler
                                ch.pipeline().addLast(new StringDecoder());//将 ByteBuf转换为字符串
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {//自定义handler
                                    @Override//读事件
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        //打印上一步转换好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        })
                //7 绑定监听端口
                .bind(9999);
    }
}

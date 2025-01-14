package advanced.netty.protocol;

import advanced.netty.protocol.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    /**
     * * 魔数，用来在第一时间判定是否是无效数据包
     * * 版本号，可以支持协议的升级
     * * 序列化算法，消息正文到底采用哪种序列化反序列化方式，可以由此扩展，例如：json、protobuf、hessian、jdk
     * * 指令类型，是登录、注册、单聊、群聊... 跟业务相关
     * * 请求序号，为了双工通信，提供异步能力
     * * 正文长度
     * * 消息正文
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        //1 4字节-魔数
        out.writeBytes(new byte[]{1,2,3,4});
        //2 1字节-版本
        out.writeByte(1);
        //3 1字节-字节的序列化方式 jdk-0;json-1
        out.writeByte(0);
        //4 1字节-指令类型
        out.writeByte(msg.getMessageType());
        //5 4字节-请求序号
        out.writeInt(msg.getSequenceId());
        //6 1字节-补位无意义，对其填充
        out.writeByte(0xff);
        //7 获取内容的字节数组
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(msg);
//        byte[] bytes = bos.toByteArray();

        byte[] bytes = SerializerEnum.Algorithm.JAVA.serializable(msg);

        //8 长度
        out.writeInt(bytes.length);
        //9 写入内容
        out.writeBytes(bytes);
        log.debug("{}",bytes.length);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte ver = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        byte supByte = in.readByte();
        int length = in.readInt();
        log.debug("{},{},{},{},{},{},{}",magicNum,ver,serializerType,messageType,sequenceId,supByte,length);
        byte[] content = new byte[length];
        in.readBytes(content, 0, length);
//        ByteArrayInputStream bis = new ByteArrayInputStream(content);
//        ObjectInputStream ois = new ObjectInputStream(bis);
//        Message message = (Message) ois.readObject();

        Message message = SerializerEnum.Algorithm.JAVA.deserializable(content, Message.class);
//        log.debug("{},{},{},{},{},{},{}",magicNum,ver,serializerType,messageType,sequenceId,supByte,length);
        log.debug("{}",message);
        out.add(message);

    }
}

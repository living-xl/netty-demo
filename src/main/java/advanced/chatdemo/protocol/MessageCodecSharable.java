package advanced.chatdemo.protocol;

import advanced.chatdemo.config.Config;
import advanced.netty.protocol.SerializerEnum;
import advanced.netty.protocol.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
/**
 * 必须和LengthFieldBasedFrameDecoder一起使用 确保接收到的数据包是完整的
 */
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        //1 4字节-魔数
        out.writeBytes(new byte[]{1,2,3,4});
        //2 1字节-版本
        out.writeByte(1);
        //3 1字节-字节的序列化方式 jdk-0;json-1;gson-2
        out.writeByte(Config.getSerializerAlgorithm().ordinal());
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

        byte[] bytes = Config.getSerializerAlgorithm().serializable(msg);
        //8 长度
        out.writeInt(bytes.length);
        //9 写入内容
        out.writeBytes(bytes);
        log.debug("{}",bytes.length);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte ver = in.readByte();
        byte serializerAlgorithm = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        byte supByte = in.readByte();
        int length = in.readInt();
//        log.debug("{},{},{},{},{},{},{}",magicNum,ver,serializerType,messageType,sequenceId,supByte,length);

        byte[] content = new byte[length];
        in.readBytes(content, 0, length);
//        ByteArrayInputStream bis = new ByteArrayInputStream(content);

        Class<?> messageClazz = Message.getMessageClass(messageType);
        SerializerEnum.Algorithm algorithm = SerializerEnum.Algorithm.values()[serializerAlgorithm];
        Object message = algorithm.deserializable(content, messageClazz);

//        ObjectInputStream ois = new ObjectInputStream(bis);
//        Message message = (Message) ois.readObject();
        log.debug("{},{},{},{},{},{},{}",magicNum,ver,serializerAlgorithm,messageType,sequenceId,supByte,length);
        log.debug("{}",message);
        out.add(message);
    }
}

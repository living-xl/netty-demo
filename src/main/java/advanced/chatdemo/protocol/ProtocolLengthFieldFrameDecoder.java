package advanced.chatdemo.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtocolLengthFieldFrameDecoder extends LengthFieldBasedFrameDecoder {
    public ProtocolLengthFieldFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProtocolLengthFieldFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}

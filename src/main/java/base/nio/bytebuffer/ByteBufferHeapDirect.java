package base.nio.bytebuffer;

import java.nio.ByteBuffer;

public class ByteBufferHeapDirect {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
        /**
         * 两种ByteBuffer的对比：
         * HeapByteBuffer: java堆内存 读写效率较低 受GC的影响
         * DirectByteBuffer: 直接内存 读写效率高（少了一次拷贝） 不受GC影响 但是分配效率低
         */
    }
}

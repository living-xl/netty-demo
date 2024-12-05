package base.nio.bytebuffer;

import java.nio.ByteBuffer;

import static base.nio.bytebuffer.ByteBufferUtil.debugAll;

public class Exam {
    public static void main(String[] args) {
        /*ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello ni hao.\n I'm zhangsan.\n How a");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("re you?\nI'm fine.\nBy");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("e.");
        int length = b1.capacity() + b2.capacity() + b3.capacity();
        ByteBuffer allb = ByteBuffer.allocate(length);
        b1.flip();
        b2.flip();
        b3.flip();
        allb.compact();
        allb.put(b1).put(b2).put(b3);
        allb.flip();
        debugAll(allb);
        String[] split = StandardCharsets.UTF_8.decode(allb).toString().split("\n");
        for (String s : split) {
            System.out.println(s);
        }*/
        ByteBuffer source = ByteBuffer.allocate(100);
        source.put("hello ni hao.\n I'm zhangsan.\n How a".getBytes());
        split(source);
        source.put("re you?\nI'm fine.\nBy".getBytes());
        split(source);
        source.put("e.\n".getBytes());
        split(source);
    }
    private static void split(ByteBuffer resouce){
        resouce.flip();
        for (int i = 0; i < resouce.limit(); i++) {
            if(resouce.get(i) == '\n'){
                int length = i+1  - resouce.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(resouce.get());
                }
                debugAll(target);
            }
        }
        resouce.compact();
    }
}

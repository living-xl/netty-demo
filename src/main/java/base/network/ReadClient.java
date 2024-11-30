package base.network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ReadClient {
    public static void main(String[] args) {
        try {

            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1",9999));

            System.out.println("waiting .....");
            int count = 0;
            while(true){
                ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
                int length = sc.read(buffer);//阻塞方法
                count+=length;
                System.out.println("read length "+length+" count "+count);
                if(length == -1){
                    break;
                }

                buffer.clear();
            }
            System.out.println("read total length "+count);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package base.nio.network;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1",9999));
            System.out.println("waiting .....");
            int num = 1;
            while(true){
                sc.write(Charset.defaultCharset().encode("1234567890"+num+"\n"));
                num++;

//                int length = sc.read(buffer);//阻塞方法
//                if(length!= 0 ){
//                    buffer.flip();
//                    debugRead(buffer);
//                    String rev = StandardCharsets.UTF_8.decode(buffer).toString();
//                    System.out.println("rev ....."+rev);
//                    buffer.clear();
//                }
                Thread.sleep(1000*1);
                if(num>=10){
                    break;
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

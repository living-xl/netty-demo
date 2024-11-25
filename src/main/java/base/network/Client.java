package base.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1",9999));
            System.out.println("waiting .....");
            int num = 1;
            while(true){
                sc.write(Charset.defaultCharset().encode("hello"+num));
                num++;
                Thread.sleep(1000*10);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

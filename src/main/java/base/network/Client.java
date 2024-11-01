package base.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("127.0.0.1",9999));
            System.out.println("waiting .....");

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

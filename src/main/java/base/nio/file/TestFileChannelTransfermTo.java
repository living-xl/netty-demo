package base.nio.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class TestFileChannelTransfermTo {
    public static void main(String[] args) {
        try {
            FileChannel form = new FileInputStream("data.txt").getChannel();
            FileChannel to = new FileOutputStream("to.txt").getChannel();
            //效率高，底层会使用零拷贝进行优化，传输上限只有2G
            form.transferTo(0,form.size(),to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

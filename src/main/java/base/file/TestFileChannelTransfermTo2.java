package base.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class TestFileChannelTransfermTo2 {
    public static void main(String[] args) {
        try {
            FileChannel form = new FileInputStream("data.txt").getChannel();
            FileChannel to = new FileOutputStream("to.txt").getChannel();
            //效率高，底层会使用零拷贝进行优化，传输上限只有2G
            long size = form.size();
            //这样可以分多次传输大于2G的文件
            for (long left = size; left > 0; ) {
                left -= form.transferTo((size - left), left, to);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

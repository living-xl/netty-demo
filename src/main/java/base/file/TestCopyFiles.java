package base.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestCopyFiles {
    public static void main(String[] args) {
        String src = "D:\\学习";
        String target = "D:\\学习2";
        try {
            Files.walk(Paths.get(src)).forEach(path -> {
                String replaceName = path.toString().replace(src, target);
                try {
                    if (Files.isDirectory(path)) {
                        Files.createDirectory(Paths.get(replaceName));
                    } else if (Files.isRegularFile(path)) {
                        Files.copy(path, Paths.get(replaceName));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

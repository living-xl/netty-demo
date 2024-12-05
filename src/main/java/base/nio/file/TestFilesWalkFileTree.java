package base.nio.file;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFilesWalkFileTree {
    public static void main(String[] args) {
        Path path = Paths.get("D://学习");
        try {
            AtomicInteger dirCount = new AtomicInteger();
            AtomicInteger fileCount = new AtomicInteger();
            Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println("====>"+dir);
                    dirCount.incrementAndGet();
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file);
                    fileCount.incrementAndGet();
                    return super.visitFile(file, attrs);
                }
            });
            System.out.println("dirCount-"+dirCount);
            System.out.println("fileCount-"+fileCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package base.nio.file;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestDelFilesWalkFileTree {
    public static void main(String[] args) {
        Path path = Paths.get("D:\\学习 - 副本");
        try {
            AtomicInteger dirCount = new AtomicInteger();
            AtomicInteger fileCount = new AtomicInteger();
            Files.walkFileTree(path,new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {


                    Files.delete(file);
                    System.out.println("del:"+file);
                    fileCount.incrementAndGet();
                    return super.visitFile(file, attrs);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    System.out.println("del====>"+dir);
                    dirCount.decrementAndGet();
                    return super.postVisitDirectory(dir, exc);
                }
            });
            System.out.println("dirCount-"+dirCount);
            System.out.println("fileCount-"+fileCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

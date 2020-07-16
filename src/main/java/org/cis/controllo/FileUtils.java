package org.cis.controllo;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {

    public static final String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();

    public static Path createAbsolutePath(String relativePath) {
        // OS independent path.
        // The root is the path of the application.
        // e.g. "\\risorse\\cacheCloneRepositories" -> Windows: "C:\Users\leo\Documents\G-Repo\risorse\cacheCloneRepositories";
        // Linux: /home/leo/Documenti/G-Repo/risorse/cacheCloneRepositories.
        if (relativePath == null) throw new IllegalArgumentException("The relativePath argument cannot be null");
        return Paths.get(FileUtils.getRootPath() + PATH_SEPARATOR, relativePath.split("\\\\"));
    }

    public static Path move(Path from, Path to) {
        if (from == null || to == null) throw new IllegalArgumentException("The argument from or to cannot be null");
        if (!FileUtils.exists(from)) throw new IllegalStateException("The path " + from + " from does not exist");
        if (FileUtils.exists(to)) throw new IllegalStateException("The path " + to + "to already exists");

        try {
            return Files.move(from, to);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Path createDirectory(Path dir) {
        if (dir == null) {
            throw new IllegalArgumentException("The dir argument cannot be null");
        }

        if (FileUtils.exists(dir)) {
            return dir;
        }

        try {
            return Files.createDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRootPath() {
        try {
            return Paths.get(".").toRealPath().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean exists(Path path) {
        if (path == null) throw new IllegalArgumentException("The path argument cannot be null");
        return Files.exists(path);
    }

    public static String extension(String fileName) {
        // TODO: REFACTORING.
        char ch;
        int len;
        if(fileName==null ||
                (len = fileName.length())==0 ||
                (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
                ch=='.' ) //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if( dotInd<=sepInd )
            return "";
        else
            return fileName.substring(dotInd+1).toLowerCase();
    }

    public static boolean deleteDirTree(Path pathDir) {
        if (pathDir == null || pathDir.toString().isEmpty()) {
            throw new IllegalArgumentException("Path directory cannot be null or empty");
        }
        if (!FileUtils.exists(pathDir)) {
            return true;
        }
        try {
            Files.walkFileTree(pathDir, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException ioe) throws IOException {
                    setPermission(dir.toFile());
                    // Files.delete(dir)
                    Files.deleteIfExists(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    setPermission(file.toFile());
                    Files.deleteIfExists(file);
                    return FileVisitResult.CONTINUE;
                }

                private void setPermission(File file) {
                    // Set application user permissions to 455.
                    file.setReadable(true);
                    file.setWritable(true);
                    file.setExecutable(false);
                    //change permission to 777 for all the users
                    //no option for group and others
                    /*file.setExecutable(true, false);
                    file.setReadable(true, false);
                    file.setWritable(true, false);*/
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

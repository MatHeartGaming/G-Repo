package org.cis.controllo;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static final String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();
    private static final DeleteFileVisitor deleteFileVisitor = new DeleteFileVisitor();

    public static Path createAbsolutePath(String relativePath) {
        // OS independent path.
        // The root is the path of the application.
        // e.g. "\\risorse\\cacheCloneRepositories" -> Windows: "C:\Users\leo\Documents\G-Repo\risorse\cacheCloneRepositories";
        // Linux: /home/leo/Documenti/G-Repo/risorse/cacheCloneRepositories.

        if (relativePath == null) throw new IllegalArgumentException("The relativePath argument cannot be null");
        return Paths.get(FileUtils.getRootPath() + PATH_SEPARATOR, relativePath.split("\\\\"));
    }

    public static boolean isDirEmpty(final Path directory) throws IOException {
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

    public static boolean moveDirTree(Path moveFrom, Path moveTo) {
        if (moveFrom == null || moveTo == null) throw new IllegalArgumentException("The argument moveFrom or moveTo cannot be null");
        if (!FileUtils.exists(moveFrom)) throw new IllegalStateException("The path " + moveFrom + " moveFrom does not exist");

        try {
            Files.walkFileTree(moveFrom, new MoveFileVisitor(moveFrom, moveTo));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyDirTree(Path copyFrom, Path copyTo) {
        if (copyFrom == null || copyTo == null) throw new IllegalArgumentException("The argument copyFrom or copyTo cannot be null");
        if (!FileUtils.exists(copyFrom)) throw new IllegalStateException("The path " + copyFrom + " copyFrom does not exist");

        try {
            Files.walkFileTree(copyFrom, new CopyFileVisitor(copyFrom, copyTo));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            Files.walkFileTree(pathDir, deleteFileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void setPermission(File file) {
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

    public static boolean unzip(String zipFilePath, String destDirectory){
        if (zipFilePath == null || zipFilePath.isEmpty()) throw new IllegalArgumentException("Zip file path cannot be null or empty");
        if (destDirectory == null || destDirectory.isEmpty()) throw new IllegalArgumentException("Destination directory cannot be null or empty");
        if (!FileUtils.exists(Paths.get(zipFilePath))) throw new IllegalStateException("The path " + zipFilePath + " zipFilePath does not exist");

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                String filePath = destDirectory + FileUtils.PATH_SEPARATOR + entry.getName();
                if (!entry.isDirectory()) {
                    // if the entry is a file, extracts it
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                    byte[] bytesIn = new byte[1024];
                    int read = 0;
                    while ((read = zipIn.read(bytesIn)) != -1) {
                        bos.write(bytesIn, 0, read);
                    }
                    bos.close();
                } else {
                    // if the entry is a directory, make the directory
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static class DeleteFileVisitor extends SimpleFileVisitor<Path> {

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
    }

    private static class MoveFileVisitor extends SimpleFileVisitor<Path> {

        private final Path moveFrom;
        private final Path moveTo;

        public MoveFileVisitor(Path moveFrom, Path moveTo) {
            this.moveFrom = moveFrom;
            this.moveTo = moveTo;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path newDir = moveTo.resolve(moveFrom.relativize(dir));
            Files.copy(dir, newDir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.move(file, moveTo.resolve(moveFrom.relativize(file)), StandardCopyOption.REPLACE_EXISTING);

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            setPermission(dir.toFile());
            Files.delete(dir);

            return FileVisitResult.CONTINUE;
        }
    }

    private static class CopyFileVisitor extends SimpleFileVisitor<Path> {

        private final Path copyFrom;
        private final Path copyTo;

        public CopyFileVisitor(Path copyFrom, Path copyTo) {
            this.copyFrom = copyFrom;
            this.copyTo = copyTo;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Files.copy(dir, copyTo.resolve(copyFrom.relativize(dir)), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, copyTo.resolve(copyFrom.relativize(file)), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            return FileVisitResult.CONTINUE;
        }

    }
}

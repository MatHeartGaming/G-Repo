package org.cis.controllo;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    String separetor = FileUtils.PATH_SEPARATOR;
    FileUtils fileUtils = new FileUtils();

    @Test
    void testCreateDirectory() {
        System.out.println("Test create a new folder");

        String pathRel ="src" + separetor + "test" + separetor + "testSources" ;
        Path toolPath = FileUtils.createAbsolutePath(pathRel);
        Path directory = fileUtils.createDirectory(toolPath);
        boolean exist = new File(directory.toString()).exists();
        assertEquals(true,exist);

    }

    @Test
    void testDeleteDirTree() {
        System.out.println("Test delete a folder Empty");

        String pathRelRoot ="src" + separetor + "test" + separetor + "testSources" ;
        Path toolPathRott = FileUtils.createAbsolutePath(pathRelRoot);
        fileUtils.createDirectory(toolPathRott);

        fileUtils.deleteDirTree(toolPathRott);

        boolean exist = new File(toolPathRott.toString()).exists();
        assertEquals(false,exist);

    }

    @Test
    void testDeleteDirTreeNotEmpty() {
        System.out.println("Test delete a folder not Empty");

        String pathRelRoot ="src" + separetor + "test" + separetor + "testSources" ;
        Path toolPathRott = FileUtils.createAbsolutePath(pathRelRoot);
        fileUtils.createDirectory(toolPathRott);

        String pathRel ="src" + separetor + "test" + separetor + "testSources" + separetor +"subPath";
        Path toolPath = FileUtils.createAbsolutePath(pathRel);
        fileUtils.createDirectory(toolPath);

        fileUtils.deleteDirTree(toolPath);

        boolean exist = new File(toolPath.toString()).exists();


        assertEquals(false,exist);

    }

    @Test
    void testPathMove() {
        System.out.println("Test delete a folder not Empty");

        String pathRelRoot ="src" + separetor + "test" + separetor + "testSources" ;
        Path toolPathRott = FileUtils.createAbsolutePath(pathRelRoot);
        fileUtils.createDirectory(toolPathRott);

        String pathRel ="src" + separetor + "test" + separetor + "testSources" + separetor +"subPath";
        Path toolPath = FileUtils.createAbsolutePath(pathRel);
        fileUtils.createDirectory(toolPath);

        fileUtils.deleteDirTree(toolPath);

        boolean exist = new File(toolPath.toString()).exists();


        assertEquals(false,exist);

    }


}
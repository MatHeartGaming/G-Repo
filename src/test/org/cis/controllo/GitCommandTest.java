package org.cis.controllo;

import org.eclipse.jgit.dircache.InvalidPathException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;

import static org.cis.controllo.FileUtils.unzip;
import static org.junit.jupiter.api.Assertions.*;

class GitCommandTest {

    private static final String RELATIVE_PATH_CLONING_DIRECTORY = "\\src\\test\\org\\cis\\controllo\\resources\\Cloning\\cacheCloneRepositories";

    private static final String RELATIVE_PATH_NOT_EXISTS = "\\pathNotExistsGHRepo";
    private String pathNotExistsGHRepo = FileUtils.createAbsolutePath(RELATIVE_PATH_NOT_EXISTS).toString();

    private Path pathCloningDirectory = FileUtils.createDirectory(FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY));

    private GitCommand gitCommand = new GitCommand();

    @Test
    void lastDateCommitExists() throws Exception {
        String prefixLastCommitDate = "_test_last_commit_date";
        Path pathCloneDirectory = FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY + "\\" + "repodriller" +  prefixLastCommitDate);
        if (!FileUtils.exists(pathCloneDirectory)) {
            unzip(pathCloneDirectory + ".zip", FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY).toString());
        }
        LocalDate actualLastCommitDate = gitCommand.lastDateCommit(String.valueOf(pathCloneDirectory));
        LocalDate expectedLastCommitDate = LocalDate.of(2018, Month.JUNE, 11);

        assertTrue(actualLastCommitDate.compareTo(expectedLastCommitDate) == 0);
    }

    @Test
    void lastDateCommitNotExists() throws Exception {
        String prefixLastCommitDate = "_test_last_commit_date_not_exists";
        Path pathCloneDirectory = FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY + "\\" + "comit-react-2019" +  prefixLastCommitDate);
        if (!FileUtils.exists(pathCloneDirectory)) {
            unzip(pathCloneDirectory + ".zip", FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY).toString());
        }
        LocalDate actualLastCommitDate = gitCommand.lastDateCommit(String.valueOf(pathCloneDirectory));

        assertNull(actualLastCommitDate);
    }

    @Test
    void lastDateCommitNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            gitCommand.lastDateCommit(null);
        });

        String expectedMessage = "Clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void lastDateCommitEmpty() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            gitCommand.lastDateCommit("");
        });

        String expectedMessage = "Clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void lastDateCommitCloneDirNotExists() {
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            gitCommand.lastDateCommit(pathNotExistsGHRepo);
        });

        String expectedMessage = "Error, clone directory " + pathNotExistsGHRepo + " not exists";
        String actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        illegalStateException = assertThrows(IllegalStateException.class, () -> {
            gitCommand.lastDateCommit("pathNotExistsGHRepo");
        });

        expectedMessage = "Error, clone directory " + "pathNotExistsGHRepo" + " not exists";
        actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void cloneRepository() {
        String cloneUrl = "https://github.com/mauricioaniche/repodriller.git";
        String prefixClone = "_clone_test";
        Path pathCloneDirectory = FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY + "\\" + "repodriller" +  prefixClone);

        try {
            gitCommand.cloneRepository(cloneUrl, String.valueOf(pathCloneDirectory), null, null);
            assertTrue(FileUtils.exists(pathCloneDirectory));
            assertFalse(FileUtils.isDirEmpty(pathCloneDirectory));
        } catch (Exception exception) {
            exception.printStackTrace();
            fail();
        } finally {
            FileUtils.deleteDirTree(pathCloneDirectory);
        }
    }

    @Test
    void cloneRepositoryPathAlreadyExists() {
        String cloneUrl = "https://github.com/mauricioaniche/repodriller.git";
        String prefixClone = "_path_already_exists_test";
        Path pathCloneDirectory = FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY + "\\" + "repodriller" +  prefixClone);
        FileUtils.createDirectory(pathCloneDirectory);

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            gitCommand.cloneRepository(cloneUrl, String.valueOf(pathCloneDirectory), null, null);
        });


        String expectedMessage = "Error, path " + pathCloneDirectory + " already exists";
        String actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void cloneRepositoryCloneUrlEmpty() {
        String cloneUrl = "";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            gitCommand.cloneRepository(cloneUrl, ".", null, null);
        });

        String expectedMessage = "Clone url cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void cloneRepositoryCloneUrlNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            gitCommand.cloneRepository(null, ".", null, null);
        });

        String expectedMessage = "Clone url cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void cloneRepositoryCloneDirectoryNull() {
        String cloneUrl = "https://github.com/mauricioaniche/repodriller.git";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            gitCommand.cloneRepository(cloneUrl, null, null, null);
        });

        String expectedMessage = "Clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void cloneRepositoryCloneDirectoryEmpty() {
        String cloneUrl = "https://github.com/mauricioaniche/repodriller.git";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            gitCommand.cloneRepository(cloneUrl, "", null, null);
        });

        String expectedMessage = "Clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void cloneRepositoryCloneUrlInvalid() {
        String cloneUrl = "https://github.com/x/y/z.git";
        Path pathCloneDirectory = FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY + "\\" + "z");
        Exception exception = assertThrows(Exception.class, () -> {
            //Rollback.
            gitCommand.cloneRepository(cloneUrl, String.valueOf(pathCloneDirectory), null, null);
        });

        assertFalse(FileUtils.exists(pathCloneDirectory));
    }

    @Test
    void cloneRepositoryInvalidCloningRepository() {
        String cloneUrl = "https://github.com/btraceio/btrace.git";
        Path pathCloneDirectory = FileUtils.createAbsolutePath(RELATIVE_PATH_CLONING_DIRECTORY + "\\" + "btrace");
        InvalidPathException InvalidPathException = assertThrows(InvalidPathException.class, () -> {
            //Rollback.
            gitCommand.cloneRepository(cloneUrl, String.valueOf(pathCloneDirectory), null, null);
        });

        assertFalse(FileUtils.exists(pathCloneDirectory));
    }

}
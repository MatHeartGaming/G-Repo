package org.cis.DAO;

import org.cis.Applicazione;
import org.cis.controllo.FileUtils;
import org.cis.modello.Repository;
import org.cis.modello.RepositoryLanguage;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DAORepositoryCSVTest {

    private static final String RELATIVE_PATH_OUTPUT_CSV_TEST = "src\\test\\org\\cis\\DAO\\csv\\output_test.csv";
    private static final String RELATIVE_PATH_INPUT_CSV_TEST = "src\\test\\org\\cis\\DAO\\csv\\input_test.csv";

    private static final String RELATIVE_PATH_FILE_NOT_EXISTS = "\\output_not_exists.csv";

    private static final DAORepositoryCSV daoRepositoryCSV = new DAORepositoryCSV();
    private static final Map<String, RepositoryLanguage> repositoryLanguageMap = new HashMap<>();

    @Test
    void updateRepositories() {
        final List<Repository> repositories = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            Repository repository = new Repository("" + i, "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", i, i);
            repositories.add(repository);
        }

        // CSV [Index, CloneDirectory, Language, Code1, Percentage1, Code2, Percentage2]
        Path outputCSV = FileUtils.createAbsolutePath(RELATIVE_PATH_OUTPUT_CSV_TEST);
        daoRepositoryCSV.updateRepositories(outputCSV, s -> {
            String[] values = s.split(",");

            if (values.length == 0) return;

            // The repository in question has never been cloned.
            if (values[1].equals("null")) return;

            Repository repository = repositories.get(Integer.parseInt(values[0]));
            repository.setCloneDirectory(values[1]);

            RepositoryLanguage repositoryLanguage = new RepositoryLanguage();
            repositoryLanguage.setLanguage(values[2]);

            if (values.length == 5) {
                // Reading the columns: Code1, Percentage1.
                repositoryLanguage.setDetection1(values[3], Double.parseDouble(values[4]));
            } else if (values.length == 7) {
                // Reading the columns:  Code1, Percentage1, Code2, Percentage2.
                repositoryLanguage.setDetection1(values[3], Double.parseDouble(values[4]));
                repositoryLanguage.setDetection2(values[5], Double.parseDouble(values[6]));
            }
            // Displays the date in the table.
            repository.setLanguageProperty(repositoryLanguage.toString());

            repositoryLanguageMap.put(repository.getId(), repositoryLanguage);
        });

        assertNull(repositoryLanguageMap.get(repositories.get(4).getId()));
        assertNull(repositoryLanguageMap.get(repositories.get(6).getId()));
        assertEquals(9, repositoryLanguageMap.size());

        assertNull(repositories.get(4).getCloneDirectory());
        assertNull(repositories.get(6).getCloneDirectory());
        assertEquals(2, repositories.stream().filter(repository -> repository.getCloneDirectory() == null).count());

        // --- Repository index 0
        Repository repository0 = repositories.get(0);
        assertEquals("C:\\testCsv\\lingua\\english\\JCSprout", repository0.getCloneDirectory());

        RepositoryLanguage repositoryLanguage0 = repositoryLanguageMap.get(repository0.getId());
        testRepositoryLanguage("english", "en", 100.0, null, 0, repositoryLanguage0);

        // --- Repository index 1
        Repository repository1 = repositories.get(1);
        assertEquals("C:\\testCsv\\lingua\\mixed\\spring-boot-examples", repository1.getCloneDirectory());

        RepositoryLanguage repositoryLanguage1 = repositoryLanguageMap.get(repository1.getId());
        testRepositoryLanguage("mixed", "en", 85.7, "no", 14.3, repositoryLanguage1);

        // --- Repository index 2
        Repository repository2 = repositories.get(2);
        assertEquals("C:\\testCsv\\lingua\\english\\SpringAll", repository2.getCloneDirectory());

        RepositoryLanguage repositoryLanguage2 = repositoryLanguageMap.get(repository2.getId());
        testRepositoryLanguage("english", "en", 100.0, null, 0, repositoryLanguage2);

        // --- Repository index 3
        Repository repository3 = repositories.get(3);
        assertEquals("C:\\testCsv\\lingua\\mixed\\SpringCloudLearning", repository3.getCloneDirectory());

        RepositoryLanguage repositoryLanguage3 = repositoryLanguageMap.get(repository3.getId());
        testRepositoryLanguage("mixed", "en", 85.7, "vi", 14.3, repositoryLanguage3);

        // --- Repository index 5
        Repository repository5 = repositories.get(5);
        assertEquals("C:\\testCsv\\lingua\\english\\Matisse", repository5.getCloneDirectory());

        RepositoryLanguage repositoryLanguage5 = repositoryLanguageMap.get(repository5.getId());
        testRepositoryLanguage("english", "en", 100.0, null, 0, repositoryLanguage5);

        // --- Repository index 7
        Repository repository7 = repositories.get(7);
        assertEquals("C:\\testCsv\\lingua\\not_english\\spring-boot-api-project-seed", repository7.getCloneDirectory());

        RepositoryLanguage repositoryLanguage7 = repositoryLanguageMap.get(repository7.getId());
        testRepositoryLanguage("not english", "zh-cn", 100.0, null, 0, repositoryLanguage7);

        // --- Repository index 8
        Repository repository8 = repositories.get(8);
        assertEquals("C:\\testCsv\\lingua\\english\\VirtualAPK", repository8.getCloneDirectory());

        RepositoryLanguage repositoryLanguage8 = repositoryLanguageMap.get(repository8.getId());
        testRepositoryLanguage("english", "en", 97.5, null, 0, repositoryLanguage8);

        // --- Repository index 9
        Repository repository9 = repositories.get(9);
        assertEquals("C:\\testCsv\\lingua\\english\\awesome-java-leetcode", repository9.getCloneDirectory());

        RepositoryLanguage repositoryLanguage9 = repositoryLanguageMap.get(repository9.getId());
        testRepositoryLanguage("unknown", null, 0, null, 0, repositoryLanguage9);

        // --- Repository index 10
        Repository repository10 = repositories.get(10);
        assertEquals("C:\\testCsv\\lingua\\not_english\\SpringBoot-Labs", repository10.getCloneDirectory());

        RepositoryLanguage repositoryLanguage10 = repositoryLanguageMap.get(repository10.getId());
        testRepositoryLanguage("not english", "zh-cn", 95.7, null, 0, repositoryLanguage10);


    }

    private void testRepositoryLanguage(String language, String code1, double percentage1, String code2, double percentage2, RepositoryLanguage actualRepositoryLanguage) {
        // --- Test Language
        assertEquals(language, actualRepositoryLanguage.getLanguage());

        // --- Test Detection 1
        assertEquals(code1, actualRepositoryLanguage.getDetection1Code());
        assertEquals(0, Double.compare(percentage1, actualRepositoryLanguage.getDetection1Percentage()));

        // --- Test Detection 2
        assertEquals(code2, actualRepositoryLanguage.getDetection2Code());
        assertEquals(0, Double.compare(percentage2, actualRepositoryLanguage.getDetection2Percentage()));
    }

    @Test
    void updateRepositoriesPathFileNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.updateRepositories(null, s -> {});
        });

        String expectedMessage = "The pathFile argument cannot be null";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateRepositoriesPathFileEmpty() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.updateRepositories(Paths.get(""), s -> {});
        });

        String expectedMessage = "The pathFile argument cannot be empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateRepositoriesPathFileNotExists() {
        final Path pathFileNotExists = FileUtils.createAbsolutePath(RELATIVE_PATH_FILE_NOT_EXISTS);
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            daoRepositoryCSV.updateRepositories(pathFileNotExists, s -> {});
        });

        String expectedMessage = "The path " + pathFileNotExists + " pathFile does not exist";
        String actualMessage = illegalStateException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateRepositoriesStringConsumerNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.updateRepositories(FileUtils.createAbsolutePath(RELATIVE_PATH_OUTPUT_CSV_TEST), null);
        });

        String expectedMessage = "The stringConsumer callback cannot be null";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveRepositories() {
        final List<Repository> repositories = new ArrayList<>();

        Repository repository0 = new Repository("0", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", 0, 0);
        repository0.setCloneDirectory("C:\\testCsv\\repo0");

        Repository repository1 = new Repository("1", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", 1, 10);
        repository1.setCloneDirectory("C:\\testCsv\\repo1");

        Repository repository2 = new Repository("2", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", 2, 20);
        repository2.setCloneDirectory("C:\\testCsv\\repo2");

        Repository repository3 = new Repository("3", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", "G-Repo DAORepositoryCSVTest", 3, 30);
        repository3.setCloneDirectory("C:\\testCsv\\repo3");

        repositories.add(repository0);
        repositories.add(repository1);
        repositories.add(repository2);
        repositories.add(repository3);

        // Create CSV file...
        // CSV [Index, CloneDirectory, Stars]
        Path inputCSV = FileUtils.createAbsolutePath(RELATIVE_PATH_INPUT_CSV_TEST);
        DAORepositoryCSV daoRepositoryCSV = Applicazione.getInstance().getDaoRepositoryCSV();
        daoRepositoryCSV.saveRepositories(inputCSV, repositories, new String[]{"Index", "CloneDirectory", "Stars"}, (repository, index) -> {
            return String.join(",", "" + index, repository.getCloneDirectory(), "" + repository.getStars());
        });

        daoRepositoryCSV.updateRepositories(inputCSV, s -> {
            String[] values = s.split(",");

            Repository repository = repositories.get(Integer.parseInt(values[0]));
            repository.setCloneDirectory(values[1]);
            repository.setStars(Integer.parseInt(values[2]));
        });

        // --- Repository index 0
        assertEquals("C:\\testCsv\\repo0", repositories.get(0).getCloneDirectory());
        assertEquals(0, repositories.get(0).getStars());

        // --- Repository index 1
        assertEquals("C:\\testCsv\\repo1", repositories.get(1).getCloneDirectory());
        assertEquals(10, repositories.get(1).getStars());

        // --- Repository index 2
        assertEquals("C:\\testCsv\\repo2", repositories.get(2).getCloneDirectory());
        assertEquals(20, repositories.get(2).getStars());

        // --- Repository index 3
        assertEquals("C:\\testCsv\\repo3", repositories.get(3).getCloneDirectory());
        assertEquals(30, repositories.get(3).getStars());

    }

    @Test
    void saveRepositoriesPathFileNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.saveRepositories(null, new ArrayList<>(), new String[]{"Index", "CloneDirectory", "Stars"}, null);
        });

        String expectedMessage = "The pathFile argument cannot be null";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveRepositoriesPathFileEmpty() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.saveRepositories(Paths.get(""), new ArrayList<>(), new String[]{"Index", "CloneDirectory", "Stars"}, null);
        });

        String expectedMessage = "The pathFile argument cannot be empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveRepositoriesListRepositoriesNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.saveRepositories(FileUtils.createAbsolutePath(RELATIVE_PATH_INPUT_CSV_TEST), null, new String[]{"Index", "CloneDirectory", "Stars"}, null);
        });

        String expectedMessage = "The repositories argument cannot be null";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveRepositoriesHeaderNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.saveRepositories(FileUtils.createAbsolutePath(RELATIVE_PATH_INPUT_CSV_TEST), new ArrayList<>(), null, null);
        });

        String expectedMessage = "The header array cannot be null";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveRepositoriesStringFunction() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            daoRepositoryCSV.saveRepositories(FileUtils.createAbsolutePath(RELATIVE_PATH_INPUT_CSV_TEST), new ArrayList<>(), new String[]{"Index", "CloneDirectory", "Stars"}, null);
        });

        String expectedMessage = "The stringFunction callback cannot be null";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
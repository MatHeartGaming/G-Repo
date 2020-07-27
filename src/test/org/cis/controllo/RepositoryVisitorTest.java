package org.cis.controllo;

import org.cis.modello.StatisticsProgrammingLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryVisitorTest {

    private RepositoryVisitor repositoryVisitor = new RepositoryVisitor();

    private static final String RELATIVE_REPO_1 = "\\src\\test\\org\\cis\\controllo\\resources\\ProgrammingLanguageDetection\\Repo1";
    private static final String RELATIVE_REPO_2 = "\\src\\test\\org\\cis\\controllo\\resources\\ProgrammingLanguageDetection\\Repo2";
    private static final String RELATIVE_REPO_3 = "\\src\\test\\org\\cis\\controllo\\resources\\ProgrammingLanguageDetection\\Repo3";

    private static final String RELATIVE_PATH_NOT_EXISTS = "\\pathNotExistsGHRepo";

    private String cloneDirectoryRepo1;
    private String cloneDirectoryRepo2;
    private String cloneDirectoryRepo3;

    private String pathNotExistsGHRepo;

    @BeforeEach
    void setUp() {
        // Repo 1.
        cloneDirectoryRepo1 = toPathStringAbsolute(RELATIVE_REPO_1);
        // Repo 2.
        cloneDirectoryRepo2 = FileUtils.createDirectory(FileUtils.createAbsolutePath(RELATIVE_REPO_2)).toString();
        // Repo 3.
        cloneDirectoryRepo3 = toPathStringAbsolute(RELATIVE_REPO_3);

        // Path not exists.
        pathNotExistsGHRepo = toPathStringAbsolute(RELATIVE_PATH_NOT_EXISTS);
    }

    @Test
    void programmingLanguageDetectionCloneDirNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            repositoryVisitor.programmingLanguageDetection(null);
        });

        String expectedMessage = "The clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void programmingLanguageDetectionCloneDirEmpty() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            repositoryVisitor.programmingLanguageDetection("");
        });

        String expectedMessage = "The clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void computeLanguagesProgrammingCloneDirNull() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            repositoryVisitor.computeLanguagesProgramming(null);
        });

        String expectedMessage = "The clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void computeLanguagesProgrammingCloneDirEmpty() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            repositoryVisitor.computeLanguagesProgramming("");
        });

        String expectedMessage = "The clone directory cannot be null or empty";
        String actualMessage = illegalArgumentException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void programmingLanguageDetectionCloneDirNotExists() {

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            repositoryVisitor.programmingLanguageDetection(pathNotExistsGHRepo);
        });

        String expectedMessage = "The path " + pathNotExistsGHRepo + " clone directory does not exist";
        String actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        illegalStateException = assertThrows(IllegalStateException.class, () -> {
            repositoryVisitor.programmingLanguageDetection("pathNotExistsGHRepo");
        });

        expectedMessage = "The path " + "pathNotExistsGHRepo" + " clone directory does not exist";
        actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void computeLanguagesProgrammingCloneDirNotExists() {

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> {
            repositoryVisitor.computeLanguagesProgramming(pathNotExistsGHRepo);
        });

        String expectedMessage = "The path " + pathNotExistsGHRepo + " clone directory does not exist";
        String actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        illegalStateException = assertThrows(IllegalStateException.class, () -> {
            repositoryVisitor.computeLanguagesProgramming("pathNotExistsGHRepo");
        });

        expectedMessage = "The path " + "pathNotExistsGHRepo" + " clone directory does not exist";
        actualMessage = illegalStateException.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void programmingLanguageDetectionRepo1() {
        StatisticsProgrammingLanguage statisticsProgrammingLanguage = repositoryVisitor.programmingLanguageDetection(cloneDirectoryRepo1);
        List<String> languagesMaximumOccurrences = new ArrayList<>();
        languagesMaximumOccurrences.add("Java");
        languagesMaximumOccurrences.add("Python");

        assertEquals(2, statisticsProgrammingLanguage.getLanguagesMaximumOccurrences().size());
        assertEquals(languagesMaximumOccurrences, statisticsProgrammingLanguage.getLanguagesMaximumOccurrences());
        assertEquals(33.3, statisticsProgrammingLanguage.getPercentage());

        assertTrue(statisticsProgrammingLanguage.existsProgrammingLanguage(s -> s.equals("Java")));
        assertTrue(statisticsProgrammingLanguage.existsProgrammingLanguage(s -> s.equals("Python")));

        assertFalse(statisticsProgrammingLanguage.existsProgrammingLanguage(s -> s.equals("HTML")));
        assertFalse(statisticsProgrammingLanguage.existsProgrammingLanguage(s -> s.equals("JavaScript")));
        assertFalse(statisticsProgrammingLanguage.existsProgrammingLanguage(s -> s.equals("Markdown")));
    }


    @Test
    void computeLanguagesProgrammingRepo1() {
        Map<String, Integer> languageProgrammingOccurrence = repositoryVisitor.computeLanguagesProgramming(cloneDirectoryRepo1);

        assertEquals(4, languageProgrammingOccurrence.size());

        assertEquals(3, languageProgrammingOccurrence.get("Python"));
        assertEquals(1, languageProgrammingOccurrence.get("HTML"));
        assertEquals(2, languageProgrammingOccurrence.get("JavaScript"));
        assertEquals(3, languageProgrammingOccurrence.get("Java"));
        assertNull(languageProgrammingOccurrence.get("Markdown"));
    }

    @Test
    void programmingLanguageDetectionRepo2() {
        StatisticsProgrammingLanguage statisticsProgrammingLanguage = repositoryVisitor.programmingLanguageDetection(cloneDirectoryRepo2);

        assertEquals(0.0, statisticsProgrammingLanguage.getPercentage());
        assertEquals(1, statisticsProgrammingLanguage.getLanguagesMaximumOccurrences().size());

        assertEquals("N.C.", statisticsProgrammingLanguage.getLanguagesMaximumOccurrences().get(0));
    }

    @Test
    void computeLanguagesProgrammingRepo2() {
        Map<String, Integer> languageProgrammingOccurrence = repositoryVisitor.computeLanguagesProgramming(cloneDirectoryRepo2);

        assertTrue(languageProgrammingOccurrence.isEmpty());
    }

    @Test
    void programmingLanguageDetectionRepo3() {
        StatisticsProgrammingLanguage statisticsProgrammingLanguage = repositoryVisitor.programmingLanguageDetection(cloneDirectoryRepo3);

        assertEquals(0.0, statisticsProgrammingLanguage.getPercentage());
        assertEquals(1, statisticsProgrammingLanguage.getLanguagesMaximumOccurrences().size());

        assertEquals("N.C.", statisticsProgrammingLanguage.getLanguagesMaximumOccurrences().get(0));
    }

    @Test
    void computeLanguagesProgrammingRepo3() {
        Map<String, Integer> languageProgrammingOccurrence = repositoryVisitor.computeLanguagesProgramming(cloneDirectoryRepo3);

        assertTrue(languageProgrammingOccurrence.isEmpty());
    }

    private String toPathStringAbsolute(String relativePath) {
        return FileUtils.createAbsolutePath(relativePath).toString();
    }
}
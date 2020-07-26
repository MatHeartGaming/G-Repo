package org.cis.controllo;

import org.cis.modello.StatisticsProgrammingLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryVisitorTest {

    private RepositoryVisitor repositoryVisitor = new RepositoryVisitor();

    public static final String RELATIVE_REPO_1 = "\\src\\test\\org\\cis\\controllo\\resources\\ProgrammingLanguageDetection\\Repo1";
    public static final String RELATIVE_REPO_2 = "\\src\\test\\org\\cis\\controllo\\resources\\ProgrammingLanguageDetection\\Repo2";
    public static final String RELATIVE_REPO_3 = "\\src\\test\\org\\cis\\controllo\\resources\\ProgrammingLanguageDetection\\Repo3";

    private String cloneDirectoryRepo1;
    private String cloneDirectoryRepo2;
    private String cloneDirectoryRepo3;

    @BeforeEach
    void setUp() {
        // Repo 1.
        cloneDirectoryRepo1 = toPathString(RELATIVE_REPO_1);
        // Repo 2.
        cloneDirectoryRepo2 = toPathString(RELATIVE_REPO_2);
        // Repo 3.
        cloneDirectoryRepo3 = toPathString(RELATIVE_REPO_3);
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

    private String toPathString(String relativePath) {
        return FileUtils.createAbsolutePath(relativePath).toString();
    }
}
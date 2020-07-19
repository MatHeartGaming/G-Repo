package org.cis.controllo;

import org.cis.DAO.ProgrammingLanguage;
import org.cis.modello.StatisticsProgrammingLanguage;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositoryVisitor {

    //todo: refactoring dei nomi...sono infelici.

    // Init map<Key=Ext, Value=LanguageName>.
    private static Map<String, String> mapExtLanguages = new ProgrammingLanguage().loadMapExtLanguages();
    private static VisitorLanguageProgramming visitorLanguageProgramming = new VisitorLanguageProgramming();

    private static class VisitorLanguageProgramming extends SimpleFileVisitor<Path> {

        // map<Key=LanguageName, Value=Occurrence>
        private Map<String, Integer> languageProgrammingOccurrence;

        public void setLanguageProgrammingOccurrence(Map<String, Integer> languageProgrammingOccurrence) {
            this.languageProgrammingOccurrence = languageProgrammingOccurrence;
        }

        public Map<String, Integer> getLanguageProgrammingOccurrence () {
            return languageProgrammingOccurrence;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (Files.isHidden(dir)) {
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!attrs.isRegularFile() || Files.isDirectory(file) || attrs.isSymbolicLink() || Files.isHidden(file)) {
                return FileVisitResult.CONTINUE;
            }

            String fileName = file.getFileName().toString();
            String ext = FileUtils.extension(fileName);
            if (ext == "") {
                return FileVisitResult.CONTINUE;
            }
            ext = "." + ext;

            // Determination of the name of the language associated with a specific extension.
            String languageName = mapExtLanguages.get(ext);
            if (languageName == null) {
                return FileVisitResult.CONTINUE;
            }

            // Calculation of the number of occurrences of the language.
            Integer occurrence = this.getLanguageProgrammingOccurrence().get(languageName);
            if (occurrence == null) {
                languageProgrammingOccurrence.put(languageName, 1);
            } else {
                languageProgrammingOccurrence.put(languageName, occurrence + 1);
            }

            return FileVisitResult.CONTINUE;
        }
    }

    public StatisticsProgrammingLanguage programmingLanguageDetection(String cloneDirectoryRepository) {
        Map<String, Integer> languageProgrammingOccurrence = this.computeLanguagesProgramming(cloneDirectoryRepository);

        // The repository can be empty, or not contain files related to programming languages ​​or markup.
        if (languageProgrammingOccurrence.isEmpty()) {
            List<String> languagesMaximumOccurrences = new ArrayList<>();
            languagesMaximumOccurrences.add("N.C.");
            return new StatisticsProgrammingLanguage(0, languagesMaximumOccurrences);
        }

        String languageNameMax = this.max(languageProgrammingOccurrence);

        List<String> languagesMaximumOccurrences = this.checkForMoreMax(languageProgrammingOccurrence, languageNameMax);

        int totalOccurrences = 0;
        for (Integer occurrence: languageProgrammingOccurrence.values()) {
            totalOccurrences = totalOccurrences + occurrence;
        }

        int occurrenceMax = languageProgrammingOccurrence.get(languageNameMax);

        double percentage = ((100.0 / totalOccurrences)
                * occurrenceMax);
        percentage = Utils.round(percentage, 1);

        return new StatisticsProgrammingLanguage(percentage, languagesMaximumOccurrences);
    }

    private List<String> checkForMoreMax(Map<String, Integer> languageProgrammingOccurrence, String languageNameMax) {
        List<String> otherLanguagesNameMax = new ArrayList<>();

        Integer occurrenceMax = languageProgrammingOccurrence.get(languageNameMax);
        for (Map.Entry<String, Integer> entry : languageProgrammingOccurrence.entrySet()) {
            String otherLanguageNameMax = entry.getKey();
            Integer otherOccurrenceMax = entry.getValue();
            if (otherOccurrenceMax.equals(occurrenceMax)) {
                otherLanguagesNameMax.add(otherLanguageNameMax);
            }
        }
        return otherLanguagesNameMax;
    }

    private String max(Map<String, Integer> languageProgrammingOccurrence) {
        String languageNameMax = (String) (languageProgrammingOccurrence.keySet().toArray())[0];

        for (Map.Entry<String, Integer> entry : languageProgrammingOccurrence.entrySet()) {
            Integer newMaxValue = languageProgrammingOccurrence.get(languageNameMax);
            if (entry.getValue() >= newMaxValue) {
                languageNameMax = entry.getKey();// new max; save the key.
            }
        }
        return languageNameMax;
    }


    public Map<String, Integer> computeLanguagesProgramming(String cloneDirectoryRepository) {
        Map<String, Integer> languageProgrammingOccurrence = new HashMap<>();
        RepositoryVisitor.visitorLanguageProgramming.setLanguageProgrammingOccurrence(languageProgrammingOccurrence);

        try {
            Files.walkFileTree(Paths.get(cloneDirectoryRepository), RepositoryVisitor.visitorLanguageProgramming);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return languageProgrammingOccurrence;
    }

}

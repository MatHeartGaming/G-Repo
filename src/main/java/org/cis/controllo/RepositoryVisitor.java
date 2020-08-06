package org.cis.controllo;

import org.cis.Constants;
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

    // Init map<Key=Ext, Value=LanguageName>.
    private static final  Map<String, String> mapExtLanguages = new ProgrammingLanguage().loadMapExtLanguages();
    private static final VisitorLanguageProgramming visitorLanguageProgramming = new VisitorLanguageProgramming();
    private String userProgrammingLanguage;

    public RepositoryVisitor() {}

    public RepositoryVisitor(String userProgrammingLanguage) {
        this.userProgrammingLanguage = userProgrammingLanguage;
    }

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
            if (ext.isEmpty()) {
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
        if (cloneDirectoryRepository == null || cloneDirectoryRepository.isEmpty()) throw new IllegalArgumentException("The clone directory cannot be null or empty");
        if (!FileUtils.exists(Paths.get(cloneDirectoryRepository))) throw new IllegalStateException("The path " + cloneDirectoryRepository + " clone directory does not exist");

        Map<String, Integer> languageProgrammingOccurrence = this.computeLanguagesProgramming(cloneDirectoryRepository);

        // The repository can be empty, or not contain files related to programming languages or markup.
        if (languageProgrammingOccurrence.isEmpty()) {
            List<String> languagesMaximumOccurrences = new ArrayList<>();
            languagesMaximumOccurrences.add("N.C.");
            return new StatisticsProgrammingLanguage(0, languagesMaximumOccurrences);
        }

        // The user specified the "language: <language name>" qualifier in the search query.
        if (this.userProgrammingLanguage != null) {
            int occurrenceUserProgrammingLanguage = 0;
            String languageNameNorm = "";

            System.out.println("User Programming Language: " + this.userProgrammingLanguage);
            // Sum of occurrences of the language specified by the user.
            // e.g. User Programming Language = Objective-C:
            // Repository1:  {Limbo|M|MATLAB|MUF|Mathematica|Mercury|Objective-C=4, C|C++|Objective-C=3, Swift=1} -> Objective-C=7 -> (87.5% - Objective-C).
            // Repository2: {Java=2, Limbo|M|MATLAB|MUF|Mathematica|Mercury|Objective-C=5, C|C++|Objective-C=6, JavaScript=1} -> Objective-C=11 -> (78.6% - Objective-C).
            for (String languageName: languageProgrammingOccurrence.keySet()) {
                int occurrence = languageProgrammingOccurrence.get(languageName);
                String[] values = languageName.split("\\|");
                for (String languageNameSplit : values) {
                    if (this.userProgrammingLanguage.toLowerCase().equals(languageNameSplit.toLowerCase())) {
                        occurrenceUserProgrammingLanguage = occurrenceUserProgrammingLanguage + occurrence;
                        languageNameNorm = languageNameSplit;
                    }
                }
            }

            if (occurrenceUserProgrammingLanguage != 0) {
                System.out.println("User programming language " + languageNameNorm + " exists in the repository");
                int totalOccurrences = getTotalOccurrences(languageProgrammingOccurrence);
                double percentage = getPercentage(totalOccurrences, occurrenceUserProgrammingLanguage);
                List<String> languagesMaximumOccurrences = new ArrayList<>();
                languagesMaximumOccurrences.add(languageNameNorm);
                return new StatisticsProgrammingLanguage(percentage, languagesMaximumOccurrences);
            }

            System.out.println("User programming language " + this.userProgrammingLanguage + " not exists in the repository");
            List<String> languagesMaximumOccurrences = new ArrayList<>();
            languagesMaximumOccurrences.add(this.userProgrammingLanguage + " " + Constants.MESSAGE_NOT_EXISTS.toLowerCase());
            return new StatisticsProgrammingLanguage(0, languagesMaximumOccurrences);
        }

        // The user did not specify the "language: <language name>" qualifier in the search query.
        System.out.println("No language qualifier ");
        String languageNameMax = this.max(languageProgrammingOccurrence);
        List<String> languagesMaximumOccurrences = this.checkForMoreMax(languageProgrammingOccurrence, languageNameMax);
        int totalOccurrences = getTotalOccurrences(languageProgrammingOccurrence);
        int occurrenceMax = languageProgrammingOccurrence.get(languageNameMax);
        double percentage = getPercentage(totalOccurrences, occurrenceMax);
        return new StatisticsProgrammingLanguage(percentage, languagesMaximumOccurrences);
    }

    private double getPercentage(int totalOccurrences, int occurrenceMax) {
        double percentage = ((100.0 / totalOccurrences)
                * occurrenceMax);
        percentage = Utils.round(percentage, 1);
        return percentage;
    }

    private int getTotalOccurrences(Map<String, Integer> languageProgrammingOccurrence) {
        int totalOccurrences = 0;
        for (Integer occurrence: languageProgrammingOccurrence.values()) {
            totalOccurrences = totalOccurrences + occurrence;
        }
        return totalOccurrences;
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
        if (cloneDirectoryRepository == null || cloneDirectoryRepository.isEmpty()) throw new IllegalArgumentException("The clone directory cannot be null or empty");
        if (!FileUtils.exists(Paths.get(cloneDirectoryRepository))) throw new IllegalStateException("The path " + cloneDirectoryRepository + " clone directory does not exist");

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

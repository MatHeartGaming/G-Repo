package org.cis.modello;

import java.util.List;
import java.util.function.Predicate;

public class StatisticsProgrammingLanguage {

    private double percentage;
    private List<String> languagesMaximumOccurrences;

    public StatisticsProgrammingLanguage(double percentage, List<String> languagesMaximumOccurrences) {
        this.percentage = percentage;
        this.languagesMaximumOccurrences = languagesMaximumOccurrences;
    }

    public double getPercentage() {
        return percentage;
    }

    public List<String> getLanguagesMaximumOccurrences() {
        //e.g. ["Java", "Assembly|C++|HTML|Motorola 68K Assembly|NASL|PHP|POV-Ray SDL|Pascal|Pawn|SourcePawn", "Haskell", "HTML"]
        return languagesMaximumOccurrences;
    }

    public boolean existsProgrammingLanguage(Predicate<String> stringPredicate) {
        if (stringPredicate == null) throw new IllegalArgumentException("The stringPredicate argument cannot be null");
        if (this.getLanguagesMaximumOccurrences() == null) throw new IllegalStateException("List Programming Languages is empty");
        return this.getLanguagesMaximumOccurrences()
                   .stream()
                   .anyMatch(stringPredicate);
    }

    @Override
    public String toString() {
        return percentage + "% - " + String.join(", ", this.languagesMaximumOccurrences);
    }

}

package org.cis.modello;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Repository {

    private long id;
    private String cloneUrl;
    private String cloneDirectory;
    private String description;
    private String file; // File JSON  a cui appartiene.
    private LocalDate lastCommitDate;
    private String programmingLanguages;// Proprietà calcolata (linguaggio con maggior percentuale).
    private List<String> listProgrammingLanguages;
    // Proprietà da visualizzare.
    private StringProperty name;
    private StringProperty urlProject;
    private StringProperty lastCommitDateProperty; // display lastCommitDate.
    private LongProperty size; // Byte.
    private StringProperty lingua; // Lingua (English, Non English, Mixed).
    private StringProperty programmingLanguagesProperty;// display programmingLanguages.

    public Repository(long id, String name, String description, String urlProject, String cloneUrl, long size) {
        String yet = "Not determined (yet)";
        this.id = id;
        this.cloneUrl = cloneUrl;
        this.description = description;
        this.name = new SimpleStringProperty(name);
        this.urlProject = new SimpleStringProperty(urlProject);
        this.size = new SimpleLongProperty(size);
        this.lingua = new SimpleStringProperty(yet);
        this.programmingLanguagesProperty = new SimpleStringProperty(yet);
        this.lastCommitDateProperty = new SimpleStringProperty(yet);
        // todo: inizializzazioni da eliminare; il menù a discesa disattiverà le voci: Lingua, Linguaggio, Data Ultimo Commit.
        this.programmingLanguages = yet;
        this.lastCommitDate = LocalDate.EPOCH;
        this.listProgrammingLanguages = new ArrayList<>();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public LocalDate getLastCommitDate() {
        return lastCommitDate;
    }

    public void setLastCommitDate(LocalDate lastCommitDate) {
        this.lastCommitDate = lastCommitDate;
    }

    public void displayLastCommitDate() {
        this.lastCommitDateProperty.set(this.getLastCommitDate().toString());
    }

    public StringProperty urlProjectProperty() {
        return urlProject;
    }

    public String getUrlProject() {
        return urlProject.get();
    }

    public long getSize() {
        return size.get();
    }

    public LongProperty sizeProperty() {
        return size;
    }

    public String getLingua() {
        return lingua.get();
    }

    public String getFile() {
        return file;
    }

    public Repository setFile(String file) {
        this.file = file;
        return this;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public String getCloneDirectory() {
        return cloneDirectory;
    }

    public void setCloneDirectory(String cloneDirectory) {
        this.cloneDirectory = cloneDirectory;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public StringProperty linguaProperty() {
        return lingua;
    }

    public String getProgrammingLanguages() {
        return programmingLanguages;
    }

    public void setProgrammingLanguages(String programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

    public void displayProgrammingLanguages() {
        this.programmingLanguagesProperty.set(this.getProgrammingLanguages());
    }

    public List<String> getListProgrammingLanguages() {
        return listProgrammingLanguages;
    }

    public void setListProgrammingLanguages(List<String> listProgrammingLanguages) {
        this.listProgrammingLanguages = listProgrammingLanguages;
    }

    public boolean existsProgrammingLanguage(Predicate<String> stringPredicate) {
        if (stringPredicate == null) throw new IllegalArgumentException("The stringPredicate argument cannot be null");
        if (this.getListProgrammingLanguages() == null) throw new IllegalStateException("List Programming Languages is empty");
        return this.getListProgrammingLanguages()
                   .stream()
                   .anyMatch(stringPredicate);
    }

    public static List<String> programmingLanguagesToList(String programmingLanguages) {
        // e.g. "Java, Assembly|C++|HTML|Motorola 68K Assembly|NASL|PHP|POV-Ray SDL|Pascal|Pawn|SourcePawn, Haskell, HTML" ->
        // to List -> [Java,  Assembly, C++, HTML, Motorola 68K Assembly, NASL, PHP, POV-Ray SDL, Pascal, Pawn, SourcePawn, Haskell, HTML]
        if (programmingLanguages == null) {
            throw new IllegalArgumentException("The programmingLanguages argument cannot be null");
        }

        return Stream.of(programmingLanguages.split("\\,"))
                     .map(s -> s.split("\\|"))
                     .flatMap(strings -> Stream.of(strings))
                     .collect(Collectors.toList());
    }

    public StringProperty programmingLanguagesPropertyProperty() {
        return programmingLanguagesProperty;
    }

    public StringProperty getDataProperty() {
        return this.lastCommitDateProperty;
    }

    public StringProperty turnIntToStringProperty() {
        String dimensione = String.valueOf(this.size);
        dimensione = dimensione.substring(dimensione.indexOf(":") + 1, dimensione.lastIndexOf("]"));
        return new SimpleStringProperty(dimensione);
    }
}

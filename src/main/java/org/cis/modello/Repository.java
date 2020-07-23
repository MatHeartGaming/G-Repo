package org.cis.modello;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Repository {

    private String id;
    private String cloneUrl;
    private String cloneDirectory;
    private String description;
    private String file; // File JSON  a cui appartiene.
    private LocalDate lastCommitDate;
    // Proprietà da visualizzare.
    private StringProperty name;
    private StringProperty urlProject;
    private StringProperty lastCommitDateProperty; // display lastCommitDate.
    private LongProperty size; // Byte.
    private StringProperty languageProperty; // Language (English, Non English, Mixed).
    private StringProperty programmingLanguagesProperty;// display programmingLanguages.
    private IntegerProperty stars;

    public Repository(String id, String name, String description, String urlProject, String cloneUrl, long size, int stars) {
        String yet = "Not determined (yet)";
        this.id = id;
        this.cloneUrl = cloneUrl;
        this.description = description;
        this.name = new SimpleStringProperty(name);
        this.urlProject = new SimpleStringProperty(urlProject);
        this.size = new SimpleLongProperty(size);
        this.languageProperty = new SimpleStringProperty(yet);
        this.programmingLanguagesProperty = new SimpleStringProperty(yet);
        this.lastCommitDateProperty = new SimpleStringProperty(yet);
        this.lastCommitDate = LocalDate.EPOCH;
        this.stars = new SimpleIntegerProperty(stars);
    }

    public Repository(String id, String name, String description, String urlProject, String cloneUrl, long size) {
        String yet = "Not determined (yet)";
        this.id = id;
        this.cloneUrl = cloneUrl;
        this.description = description;
        this.name = new SimpleStringProperty(name);
        this.urlProject = new SimpleStringProperty(urlProject);
        this.size = new SimpleLongProperty(size);
        this.languageProperty = new SimpleStringProperty(yet);
        this.programmingLanguagesProperty = new SimpleStringProperty(yet);
        this.lastCommitDateProperty = new SimpleStringProperty(yet);
        this.lastCommitDate = LocalDate.EPOCH;
        this.stars = new SimpleIntegerProperty(0);
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

    public String getSizeString() {
        return String.valueOf(size.get());
    }

    public LongProperty sizeProperty() {
        return size;
    }

    public String getLanguageProperty() {
        return languageProperty.get();
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

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public StringProperty languagePropertyProperty() {
        return languageProperty;
    }

    public void setLanguageProperty(String languageProperty) {
        this.languageProperty.set(languageProperty);
    }

    public int getStars() {
        return stars.get();
    }

    public SimpleStringProperty starsProperty() {
        String stelline = String.valueOf(stars.get());
        return new SimpleStringProperty(stelline);
    }

    public void setStars(int stars) {
        this.stars.set(stars);
    }

    public void displayProgrammingLanguages(String programmingLanguage) {
        this.programmingLanguagesProperty.set(programmingLanguage);
    }

    public StringProperty programmingLanguagesPropertyProperty() {
        return programmingLanguagesProperty;
    }

    public String getProgrammingLanguageProperty() {
        return this.programmingLanguagesProperty.get();
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

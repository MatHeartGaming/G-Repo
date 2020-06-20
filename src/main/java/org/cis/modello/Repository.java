package org.cis.modello;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Repository {

    private long id;
    private String cloneUrl;
    private String cloneDirectory;
    private String description;
    private String file; // File JSON  a cui appartiene.
    // Proprietà da visualizzare.
    private StringProperty name;
    private LocalDate lastCommitDate; //Attributo JSON "pushed_at"?.
    private StringProperty urlProject;
    private LongProperty size; // Byte.
    private StringProperty lingua; // Lingua (English, Non English, Mixed).
    private StringProperty type; // Tipologia repository (Software development, Experimental, Storage...).
    private StringProperty programmingLanguage;// Proprietà calcolata (linguaggio con maggior percentuale).

    public Repository(long id, String name, String description, String urlProject,  String cloneUrl, long size) {
        this.id = id;
        this.cloneUrl = cloneUrl;
        this.description = description;
        this.name = new SimpleStringProperty(name);
        this.urlProject = new SimpleStringProperty(urlProject);
        this.size = new SimpleLongProperty(size);
    }

    public Repository(String name, LocalDate lastCommitDate, String urlProject, long size, String lingua, String programmingLanguage) {
        this.name = new SimpleStringProperty(name);
        this.lastCommitDate = lastCommitDate;
        this.urlProject = new SimpleStringProperty(urlProject);
        this.size = new SimpleLongProperty(size);
        this.lingua = new SimpleStringProperty(lingua);
        this.programmingLanguage = new SimpleStringProperty(programmingLanguage);
    }

    public Repository(String name, String version) {
        this.name = new SimpleStringProperty(name);
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

    public StringProperty typeProperty() {
        return type;
    }

    public StringProperty linguaProperty() {
        return lingua;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage.get();
    }

    public StringProperty programmingLanguageProperty() {
        return programmingLanguage;
    }

    public StringProperty getDataProperty() {
        return new SimpleStringProperty(this.lastCommitDate.toString());
    }

    public StringProperty turnIntToStringProperty() {
        String dimensione = String.valueOf(this.size);
        dimensione = dimensione.substring(dimensione.indexOf(":") + 1, dimensione.lastIndexOf("]"));
        return new SimpleStringProperty(dimensione);
    }
}
